package com.htstar.ovms.device.service.impl;

import com.htstar.ovms.admin.api.feign.SysUserRoleFeign;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.device.api.constant.AlarmTypeConstant;
import com.htstar.ovms.device.api.entity.CarFenceRemind;
import com.htstar.ovms.device.api.entity.DeviceAlarm;
import com.htstar.ovms.device.api.vo.FenceVO;
import com.htstar.ovms.device.protoco.GpsItemTp;
import com.htstar.ovms.device.protoco.ObdGpsProtoco;
import com.htstar.ovms.device.protoco.ObdStatDataTp;
import com.htstar.ovms.device.service.*;
import com.htstar.ovms.device.util.DateXQ;
import com.htstar.ovms.device.util.GeographyUtil;
import com.htstar.ovms.enterprise.api.dto.CarSchedulingTimeWhereDTO;
import com.htstar.ovms.enterprise.api.feign.CarSchedulingTimeFeign;
import com.htstar.ovms.msg.api.constant.MsgTypeConstant;
import com.htstar.ovms.msg.api.entity.MsgPushLog;
import com.htstar.ovms.msg.api.feign.MsgAppPushFeign;
import com.htstar.ovms.msg.api.req.BroadAppPushByEtpReq;
import com.htstar.ovms.msg.api.req.SingleAppPushReq;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/8/1310:51
 */
@Service
@Slf4j
public class ObdGpsProcotoListenerServiceImpl implements ObdGpsProcotoListenerService {

    /**
     * 围栏
     */
    @Autowired
    private FenceService fenceService;

    /**
     * 围栏推送
     */
    @Autowired
    private CarFenceRemindService carFenceRemindService;

    @Autowired
    private DeviceAlarmService deviceAlarmService;
    @Autowired
    MsgAppPushFeign msgAppPushFeign;
    @Autowired
    DeviceService deviceService;
    @Autowired
    DeviceAlarmStatService deviceAlarmStatService;

    @Autowired
    DeviceAlarmGpsService deviceAlarmGpsService;

    @Autowired
    SysUserRoleFeign SysUserRoleFeign;

    @Override
    public void ObdGpsProcotoListenerMsgPush(int carStatus,
                                             String deviceSn,
                                             GpsItemTp lastGps,
                                             ObdGpsProtoco obdGpsProtoco,
                                             int tripId,
                                             ObdStatDataTp obdStatDataTp,
                                             int userId) {
        //推送消息
        BroadAppPushByEtpReq singleAppPushReq = new BroadAppPushByEtpReq();
        //当前警报地址
        String addr = deviceAlarmGpsService.saveGpsAddr(obdGpsProtoco.getObdGpsDataTp());

        //2.更新设备最新状态
        lastGps = obdGpsProtoco.getObdGpsDataTp().getLastGpsItemTp();
        //检查围栏（这里做rides缓存）
        List<FenceVO> fenceList = fenceService.queryFenceList(deviceSn);
        //获取当前企业
        int etpIds = deviceService.getEtpIds(deviceSn);
        //当前绑定设备的车辆
        String licCode = deviceService.getLicCode(deviceSn);
        if (null != fenceList && !fenceList.isEmpty()) {
            for (FenceVO fenceVO : fenceList) {
                int remindType = fenceVO.getRemindType().intValue();
                if (remindType == 0) continue;
                //需要推送
                //提醒类型：0=不提醒；2=驶入提醒；3=驶出提醒；4=驶入驶出都提醒；
                //0 圆形 1 正方形
                boolean status = false;
                if (0 == fenceVO.getFenceType()) {
                    status = GeographyUtil.getOutsideStatus(fenceVO.getLat(), fenceVO.getLng(),
                            lastGps.getLat(), lastGps.getLng(), fenceVO.getRadius());
                } else if (1 == fenceVO.getFenceType()) {
                    status = GeographyUtil.isInPolygon(lastGps.getLng(), lastGps.getLat(),
                            fenceVO.getOrdinates().split(","), fenceVO.getAbscissas().split(","));
                }
                CarFenceRemind remind1 = carFenceRemindService.getLastRemindByTripId(tripId);

                boolean flag = false;
                int type = 2;
                if (remind1 == null && status == true) {
                    flag = true;
                } else {
                    if (remind1 != null) {
                        if (remind1.getRemindType() == 2 && status == true) continue;//判断已经驶入不需要提醒
                        if (remind1.getRemindType() == 3 && status == false) continue;//判断已经驶出不需要提醒
                        //驶入提醒
                        if (status == true && (remindType == 2 || remindType == 4) && remind1.getRemindType() == 3) {
                            flag = true;
                            //驶出提醒
                        } else if (status == false && (remindType == 3 || remindType == 4) && remind1.getRemindType() == 2) {
                            flag = true;
                            type = 3;

                        }
                    }
                }
                //第一次警告添加
                if (flag) {
                    CarFenceRemind carFenceRemind = new CarFenceRemind();
                    carFenceRemind.setCarId(fenceVO.getCarId());
                    carFenceRemind.setTripId(tripId);
                    carFenceRemind.setRemindType(type);
                    carFenceRemind.setCreateTime(LocalDateTime.now());
                    carFenceRemindService.save(carFenceRemind);
                }

                //在围栏内
                LocalDateTime now = OvmDateUtil.getCstNow();
                if (status == true && (remindType == 2 || remindType == 4)) {
                    //获取当前行程的最后一次警情推送
                    CarFenceRemind remind = carFenceRemindService.getLastRemindByTripId(tripId);
                    log.info("驶入");
                    if (null != remind && (remind.getRemindType().intValue() == remindType || remindType == 4)) {//需要推送驶入
                        //TODO 推送消息
                        log.info("推送围栏驶入消息到APP：{}", remind);
                        DeviceAlarm alarm = new DeviceAlarm();
                        alarm.setDeviceSn(obdGpsProtoco.getDeviceSn());
                        alarm.setAlarmNo(1L);
                        alarm.setRcvTime(now);
                        alarm.setStaTime(now);
                        alarm.setEndTime(remind.getCreateTime());
                        alarm.setAlarmFlag(1);
                        alarm.setAlarmType(AlarmTypeConstant.FENCE);
                        alarm.setAlarmDesc("0");
                        obdGpsProtoco.getObdGpsDataTp().setLastGpsItemTp(lastGps);
                        Integer integer = deviceAlarmGpsService.saveGps(obdGpsProtoco.getObdGpsDataTp());
                        alarm.setStaGpsId(integer);
                        Integer integer1 = deviceAlarmStatService.saveStat(obdStatDataTp);
                        alarm.setStaStatId(integer1);
                        alarm.setIsRead(0);
                        deviceAlarmService.save(alarm);
                        //推送消息
                        singleAppPushReq.setMsgType(MsgTypeConstant.REMINDER_MESSAGE);
                        singleAppPushReq.setTitle("越界警报");
                        singleAppPushReq.setEtpId(etpIds);
                        singleAppPushReq.setContent("车牌号[" + licCode + "]于" + OvmDateUtil.format() + "在" + addr + "疑似越界，请及时确认！-驶入");
                        try {
                            msgAppPushFeign.EdpIdtoSingdemos(singleAppPushReq, SecurityConstants.FROM_IN);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                } else {//在围栏外
                    if (status == false && (remindType == 3 || remindType == 4)) {
                        //获取当前行程的最后一次警情推送
                        CarFenceRemind remind = carFenceRemindService.getLastRemindByTripId(tripId);
                        log.info("驶出");
                        if (null != remind && (remind.getRemindType().intValue() == remindType || remindType == 4)) {//需要推送驶入
                            //TODO 推送消息
                            log.info("推送围栏驶出消息到APP：{}", remind);
                            DeviceAlarm alarm = new DeviceAlarm();
                            alarm.setDeviceSn(obdGpsProtoco.getDeviceSn());
                            alarm.setAlarmNo(1L);
                            alarm.setRcvTime(now);
                            alarm.setStaTime(now);
                            alarm.setEndTime(remind.getCreateTime());
                            alarm.setAlarmFlag(1);
                            alarm.setAlarmType(AlarmTypeConstant.FENCE);
                            alarm.setAlarmDesc("0");
                            obdGpsProtoco.getObdGpsDataTp().setLastGpsItemTp(lastGps);
                            Integer integer = deviceAlarmGpsService.saveGps(obdGpsProtoco.getObdGpsDataTp());
                            alarm.setStaGpsId(integer);
                            Integer integer1 = deviceAlarmStatService.saveStat(obdStatDataTp);
                            alarm.setStaStatId(integer1);
                            alarm.setIsRead(0);
                            deviceAlarmService.save(alarm);
                            //推送消息
                            singleAppPushReq.setMsgType(MsgTypeConstant.REMINDER_MESSAGE);
                            singleAppPushReq.setTitle("越界警报");
                            singleAppPushReq.setEtpId(etpIds);
                            singleAppPushReq.setContent("车牌号[" +licCode+ "]于" + OvmDateUtil.format() + "在" + addr + "疑似越界，请及时确认！-驶出");
                            try {
                                msgAppPushFeign.EdpIdtoSingdemos(singleAppPushReq, SecurityConstants.FROM_IN);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

        }
    }
}
