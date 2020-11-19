package com.htstar.ovms.device.handle.listener;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.device.api.constant.AlarmTypeConstant;
import com.htstar.ovms.device.api.entity.CarFenceRemind;
import com.htstar.ovms.device.api.entity.DeviceAlarm;
import com.htstar.ovms.device.api.entity.DeviceLastData;
import com.htstar.ovms.device.api.entity.Fence;
import com.htstar.ovms.device.api.vo.FenceVO;
import com.htstar.ovms.device.handle.event.ObdGpsProcotoEvent;
import com.htstar.ovms.device.protoco.GpsItemTp;
import com.htstar.ovms.device.protoco.ObdGpsProtoco;
import com.htstar.ovms.device.protoco.ObdStatDataTp;
import com.htstar.ovms.device.service.*;
import com.htstar.ovms.device.util.GeographyUtil;
import com.htstar.ovms.msg.api.feign.MsgAppPushFeign;
import com.htstar.ovms.msg.api.req.SingleAppPushReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:ObdGpsData 监听
 * Author: flr
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 */
@Component
@Slf4j
public class ObdGpsProcotoListener {
    @Autowired
    private MongoDbHandleService mongoDbHandleService;

    /**
     * 设备最新状态
     */
    @Autowired
    private DeviceLastDataService deviceLastDataService;


    @Autowired
    DeviceService deviceService;

    @Autowired
    ObdGpsProcotoListenerService obdGpsProcotoListenerService;

    @EventListener
    @Async
    public void onApplicationEvent(ObdGpsProcotoEvent obdGpsDataEvent) {
        log.debug("监听到【GPS】原始数据为：{}", obdGpsDataEvent);

        ObdGpsProtoco obdGpsProtoco = obdGpsDataEvent.getObdGpsProtoco();
        String deviceSn = obdGpsProtoco.getDeviceSn();
        int userId = deviceService.getuserId(deviceSn);//根据设备找车辆最终找到所属车辆的用户ID
        //1.原始GPS信息入库mongodb
        mongoDbHandleService.saveGpsToMongodb(obdGpsProtoco.getObdGpsDataTp());

        //2.更新设备最新状态
        GpsItemTp lastGps = obdGpsProtoco.getObdGpsDataTp().getLastGpsItemTp();
        if (null != lastGps && obdGpsProtoco.getFlag().intValue() == 0) {
            ObdStatDataTp obdStatDataTp = obdGpsProtoco.getObdStatDataTp();
            DeviceLastData deviceLastData = new DeviceLastData();
            deviceLastData.setDeviceSn(deviceSn);
            deviceLastData.setLat(lastGps.getLat());
            deviceLastData.setLng(lastGps.getLng());
            deviceLastData.setGpsTime(lastGps.getGpsTime());
            float speed = lastGps.getSpeed();
//            deviceLastData.setSpeed(speed); //速度也取工况
            deviceLastData.setDirection(lastGps.getDirection());
            deviceLastData.setLatitudeWay(lastGps.getLatitudeWay());
            deviceLastData.setLongitudeWay(lastGps.getLongitudeWay());
            deviceLastData.setPositionStarNum(lastGps.getPositionStarNum());
            deviceLastData.setLastAcconTime(obdStatDataTp.getLastAcconTime());
            deviceLastData.setTotalFuel(obdStatDataTp.getTotalFuel());
            deviceLastData.setCurrentFuel(obdStatDataTp.getCurrentFuel());
            deviceLastData.setTotalTripMileage((double) obdStatDataTp.getTotalTripMileage());
            deviceLastData.setCurrentTripMileage((double) obdStatDataTp.getCurrentTripMileage());
            deviceLastData.setGpsFlag(lastGps.getGpsFlag());
            int rpm = obdGpsProtoco.getObdRpmDataTp().getRpm();
            // 转速读取工况中的
//            deviceLastData.setRpm(rpm);
//            log.info("gps中的速度：{}，转速：{}",speed,rpm);

            // 判断汽车运行状态
            int carStatus = 0; // 0:熄火 转速无，车速无 1：运行 转速有，车速有 2：怠速 车速无，转速有
            if (rpm > 0 && speed > 0) {
                carStatus = 1;
            } else if (rpm > 0 && speed <= 0) {
                carStatus = 2;
            }

            deviceLastData.setCarStatus(carStatus);
            deviceLastDataService.lambdaUpdate().eq(DeviceLastData::getDeviceSn, deviceSn).update(deviceLastData);

            int tripId = obdGpsProtoco.getTripId();
            /**
             * 检测驶入驶出进行推送
             */
            obdGpsProcotoListenerService.ObdGpsProcotoListenerMsgPush(deviceSn,lastGps,obdGpsProtoco,tripId,obdStatDataTp,userId);
        }

    }
}

