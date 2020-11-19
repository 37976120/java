package com.htstar.ovms.device.handle.listener;

import com.htstar.ovms.admin.api.feign.SysUserRoleFeign;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.OvmDateUtil;

import com.htstar.ovms.device.api.constant.AlarmTypeConstant;
import com.htstar.ovms.device.api.constant.ObdAlarmTypeConstant;
import com.htstar.ovms.device.api.entity.DeviceAlarm;
import com.htstar.ovms.device.api.entity.DeviceTrip;
import com.htstar.ovms.device.handle.event.ObdAlarmProcotoEvent;
import com.htstar.ovms.device.protoco.AlarmDataTp;
import com.htstar.ovms.device.protoco.ObdAlarmProtoco;
import com.htstar.ovms.device.service.*;
import com.htstar.ovms.device.util.DateXQ;
import com.htstar.ovms.enterprise.api.dto.CarSchedulingTimeWhereDTO;
import com.htstar.ovms.enterprise.api.feign.CarInfoFeign;
import com.htstar.ovms.enterprise.api.feign.CarSchedulingTimeFeign;
import com.htstar.ovms.msg.api.constant.MsgTypeConstant;
import com.htstar.ovms.msg.api.entity.MsgPushLog;
import com.htstar.ovms.msg.api.feign.MsgAppPushFeign;
import com.htstar.ovms.msg.api.req.BroadAppPushByEtpReq;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 警情监听
 * Author: flr
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 */
@Slf4j
@Component
public class ObdAlarmProcotoListener {


    @Autowired
    private MongoDbHandleService mongoDbHandleService;

    @Autowired
    private DeviceAlarmGpsService deviceAlarmGpsService;

    @Autowired
    private DeviceAlarmStatService deviceAlarmStatService;

    @Autowired
    private DeviceAlarmService deviceAlarmService;

    @Autowired
    private DeviceTripService deviceTripService;

    @Autowired
    MsgAppPushFeign msgAppPushFeign;

    @Autowired
    DeviceService deviceService;

    @Autowired
    SysUserRoleFeign SysUserRoleFeign;

    @Autowired
    CarInfoFeign carInfoFeign;


    @Autowired
    CarSchedulingTimeFeign carSchedulingTimeFeign;

    @EventListener
    @Async
    public void onApplicationEvent(ObdAlarmProcotoEvent obdAlarmEvent) {
        log.debug("监听到【警情】原始数据为：{}", obdAlarmEvent);
        String deviceSn = obdAlarmEvent.getObdAlarmProtoco().getDeviceSn();
        ObdAlarmProtoco obdAlarmProtoco = obdAlarmEvent.getObdAlarmProtoco();
        //推送消息
        BroadAppPushByEtpReq singleAppPushReq = new BroadAppPushByEtpReq();
        //1.关于警情的GPS数据入Mysql库
        Integer alarmGpsId = deviceAlarmGpsService.saveGps(obdAlarmProtoco.getObdGpsDataTp());

        //2.关于警情的STAT 数据入Mysql库
        Integer alarmStatId = deviceAlarmStatService.saveStat(obdAlarmProtoco.getObdStatDataTp());
        //当前警报地址
        String addr = deviceAlarmGpsService.saveGpsAddr(obdAlarmProtoco.getObdGpsDataTp());
        //当前绑定设备的车辆
        String licCode = deviceService.getLicCode(deviceSn);
        //当前绑定设备的车辆id
        Integer carId = deviceService.getCarId(deviceSn);
        //3.入库警情
        List<AlarmDataTp> alarmDataTpList = obdAlarmProtoco.getAlarmDataTpList();

        List<DeviceAlarm> deviceAlarmList = new ArrayList<>();
        // 急加速的次数
        int sharpAccelerate = 0;
        // 急减速的次数
        int sharpSlowdown = 0;
        // 急转弯的次数
        int sharpSwerve = 0;
        // 超速的次数
        int overSpeed = 0;

        Integer alarmCount = obdAlarmProtoco.getAlarmCount();
        if (null == alarmCount || alarmCount.intValue() == 0) {
            return;
        }
        BroadAppPushByEtpReq sing = new BroadAppPushByEtpReq(); //企业推送对象
        sing.setAppCarId(carId);
        sing.setRemindType(3);
        int etpIds = deviceService.getEtpIds(deviceSn);
        sing.setEtpId(etpIds);
        //消息提醒
        sing.setMsgType(MsgTypeConstant.REMINDER_MESSAGE);

        List<MsgPushLog> logList = new ArrayList<>();
        List<Integer> list = null;
        try {
            list = SysUserRoleFeign.queryAdminIdListInner(etpIds, SecurityConstants.FROM_IN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (AlarmDataTp alarmData : alarmDataTpList) {
            /**
             * 0=警情结束；1=新警情；
             * 持续警情警情（有结束标识0上来的）有：超速、低电压、水温告警、停车未熄火、转速高、尾气超标、疲劳驾驶、MIL故障告警；
             * 及时警情（无结束标识0）：急加速、急减速、拖吊告警、上电告警、急变道、急转弯、危险驾驶、震动告警、断电告警、区域告警、紧急告警、碰撞告警、防拆告警、非法进入告警、非法点火告警、OBD剪线告警、点火告警、熄火告警
             */

            int alarmFlag = alarmData.getNewAlarmFlag().intValue();

            /**
             * 警情类型	U8	1
             */
            int alarmType = alarmData.getAlarmType().intValue();
            if (alarmFlag == 1) {
                DeviceAlarm deviceAlarm = new DeviceAlarm();
                deviceAlarm.setDeviceSn(deviceSn);
                deviceAlarm.setStaGpsId(alarmGpsId);
                deviceAlarm.setStaStatId(alarmStatId);
                deviceAlarm.setAlarmNo(obdAlarmProtoco.getAlarmNo());
                deviceAlarm.setRcvTime(obdAlarmProtoco.getRevTime());
                if (null != alarmData.getAlarmDesc()) {
                    deviceAlarm.setAlarmDesc(alarmData.getAlarmDesc().toString());
                }
                deviceAlarm.setAlarmFlag(alarmFlag);
                deviceAlarm.setAlarmType(alarmType);
                deviceAlarm.setStaTime(obdAlarmProtoco.getRevTime());
                deviceAlarm.setEndTime(obdAlarmProtoco.getRevTime());
                deviceAlarmList.add(deviceAlarm);
                log.info("警情类型" + deviceAlarm.getAlarmType());
                /**
                 * 告警类型:1:超速告警,2:低电压告警,3:水温告警,7:拖吊告警,14:断电告警
                 */
                switch (deviceAlarm.getAlarmType()) {
                    case AlarmTypeConstant.FENCE:
                        for (Integer integer : list) {
                            MsgPushLog msgPushLog = new MsgPushLog();//消息持久化对象
                            msgPushLog.setUserId(integer);
                            msgPushLog.setTitle("超速告警");
                            msgPushLog.setMsgType(MsgTypeConstant.REMINDER_MESSAGE);
                            msgPushLog.setContent("车牌号[" + licCode + "]于" + OvmDateUtil.format() + "在" + addr + "超速，请及时确认！");
                            msgPushLog.setRemindType(3);
                            msgPushLog.setAppCarId(carId);
                            logList.add(msgPushLog);
                        }
                        if (!logList.isEmpty()) {
                            try {
                                msgAppPushFeign.saveMsgPushLogs(logList, SecurityConstants.FROM_IN);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case AlarmTypeConstant.LOW_BATTERY:
                        sing.setTitle("低电压告警");
                        sing.setContent("车牌号[" + licCode + "]车辆电压过低，请及时确认！");
                        break;
                    case AlarmTypeConstant.WATER_TEMP:
                        sing.setTitle("水温告警");
                        sing.setContent("车牌号[" + licCode + "]车辆出现水温异常，请及时确认！");
                        break;
                    case AlarmTypeConstant.TOW:
                        sing.setTitle("拖吊告警");
                        sing.setContent("车牌号[" + licCode + "]于" + OvmDateUtil.format() + "在" + addr + "疑似发生拖吊，请及时确认！");
                        break;

                    case AlarmTypeConstant.PULL_OUT:
                        sing.setTitle("断电告警");
                        sing.setContent("车牌号[" + licCode + "]车辆设备已被拔出，请及时确认！");
                        break;
                }
                if (sing.getTitle() != null) {
                    try {
                        msgAppPushFeign.EdpIdtoSingdemos(sing, SecurityConstants.FROM_IN);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                DeviceAlarm up = deviceAlarmService.getEnt(deviceSn, obdAlarmProtoco.getAlarmNo(), alarmData.getAlarmType());
                if (null != up) {
                    up.setEndGpsId(alarmGpsId);
                    up.setEndStatId(alarmStatId);
                    up.setEndTime(obdAlarmProtoco.getRevTime());
                }

                deviceAlarmService.updateById(up);
            }

            //4. 点火报警，增加新的行程

            if (alarmType == 22) {
                log.info("点火警情");
                CarSchedulingTimeWhereDTO cardto = new CarSchedulingTimeWhereDTO();
                LocalTime lastAcconTime = obdAlarmProtoco.getObdStatDataTp().getLastAcconTime().toLocalTime();
                cardto.setStatime(lastAcconTime.toString());
                cardto.setEndtime(lastAcconTime.toString());
                cardto.setLicCode(licCode);
                cardto.setNotScheduleWeek(DateXQ.XQ());
                int count = carSchedulingTimeFeign.getlicCodeCount(licCode, SecurityConstants.FROM_IN);//判断排班是否有车
                int count1 = carSchedulingTimeFeign.getCount(cardto, SecurityConstants.FROM_IN);//判断车辆是否当前时间排班
                if (carSchedulingTimeFeign.getsetting(licCode,SecurityConstants.FROM_IN) > 0) { //车辆是否已申请
                    if (count != 0) {
                        //不可以使用，判断日期
                        if (count1 == 0) {
                            //没有排班，违规操作

                            //推送消息
                            singleAppPushReq.setMsgType(MsgTypeConstant.REMINDER_MESSAGE);
                            singleAppPushReq.setTitle("违规警报");
                            singleAppPushReq.setEtpId(etpIds);
                            singleAppPushReq.setAppCarId(carId);
                            singleAppPushReq.setContent("车牌号[" + licCode + "]于" + OvmDateUtil.format() + "在" + addr + "疑似违规，请及时确认！");
                            if (singleAppPushReq.getTitle() != null) {
                                try {
                                    msgAppPushFeign.EdpIdtoSingdemos(singleAppPushReq, SecurityConstants.FROM_IN);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    //推送消息
                    singleAppPushReq.setMsgType(MsgTypeConstant.REMINDER_MESSAGE);
                    singleAppPushReq.setTitle("违规警报");
                    singleAppPushReq.setEtpId(etpIds);
                    singleAppPushReq.setAppCarId(carId);
                    singleAppPushReq.setContent("车牌号[" + licCode + "]于" + OvmDateUtil.format() + "在" + addr + "疑似违规，请及时确认！");
                    if (singleAppPushReq.getTitle() != null) {
                        try {
                            msgAppPushFeign.EdpIdtoSingdemos(singleAppPushReq, SecurityConstants.FROM_IN);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                    deviceTripService.dealProcotoTrip(deviceSn, obdAlarmProtoco.getObdGpsDataTp(), obdAlarmProtoco.getObdStatDataTp());
                }

                //对行程的报警进行统计
                if (alarmFlag == 1) {
                    switch (alarmType) {
                        case ObdAlarmTypeConstant.OVER_SPEED:
                            overSpeed++;
                            continue;

                        case ObdAlarmTypeConstant.SHARP_ACCELERATE:
                            sharpAccelerate++;
                            continue;

                        case ObdAlarmTypeConstant.SHARP_SLOWDOWN:
                            sharpSlowdown++;
                            continue;

                        case ObdAlarmTypeConstant.SHARP_SWERVE:
                            sharpSwerve++;
                            continue;

                        default:
                            continue;
                    }
                }

            }

            deviceAlarmService.saveBatch(deviceAlarmList);

            //更新统计信息
            if (sharpAccelerate != 0
                    || sharpSlowdown != 0
                    || sharpSwerve != 0
                    || overSpeed != 0) {
                DeviceTrip deviceTrip = new DeviceTrip();
                deviceTrip.setDeviceSn(deviceSn);
                deviceTrip.setSharpAccelerate(sharpAccelerate);
                deviceTrip.setSharpSlowdown(sharpSlowdown);
                deviceTrip.setSharpSwerve(sharpSwerve);
                deviceTrip.setOverSpeed(overSpeed);
                deviceTrip.setStaTime(obdAlarmProtoco.getObdStatDataTp().getLastAcconTime());

                deviceTripService.updateAlarmCount(deviceTrip);
            }
        }

    }