package com.htstar.ovms.device.handle.listener;

import com.htstar.ovms.device.api.entity.DeviceLastData;
import com.htstar.ovms.device.handle.event.ObdConditionProcotoEvent;
import com.htstar.ovms.device.protoco.ObdConditionProcoto;
import com.htstar.ovms.device.protoco.ObdStatDataTp;
import com.htstar.ovms.device.service.DeviceLastDataService;
import com.htstar.ovms.device.service.MongoDbHandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Description: OBD工况事件监听处理
 * Author: flr
 * Date: Created in 2020/6/22
 * Company: 航通星空
 * Modified By:
 */
@Component
@Slf4j
public class ObdConditionProcotoListener {

    @Autowired
    private MongoDbHandleService mongoDbHandleService;

    /**
     * 设备最新状态
     */
    @Autowired
    private DeviceLastDataService deviceLastDataService;

    @EventListener
    @Async
    public void onApplicationEvent(ObdConditionProcotoEvent obdConditionProcotoEvent) {
        log.debug("监听到【工况】原始数据为：{}",obdConditionProcotoEvent);
        ObdConditionProcoto obdConditionProcoto = obdConditionProcotoEvent.getObdConditionProcoto();


        String deviceSn = obdConditionProcoto.getDeviceSn();
        //1.原始工况信息入库mongodb
        mongoDbHandleService.saveConditionToMongodb(obdConditionProcoto);

        //2.更新设备最新状态
        ObdStatDataTp obdStatDataTp = obdConditionProcoto.getObdStatDataTp();

        DeviceLastData deviceLastData = new DeviceLastData();
        deviceLastData.setDeviceSn(deviceSn);
        float speed = obdConditionProcoto.getSpeed();
        deviceLastData.setSpeed(speed);
        deviceLastData.setLastAcconTime(obdStatDataTp.getLastAcconTime());
        deviceLastData.setTotalFuel(obdStatDataTp.getTotalFuel());
        deviceLastData.setCurrentFuel(obdStatDataTp.getCurrentFuel());
        deviceLastData.setTotalTripMileage((double)obdStatDataTp.getTotalTripMileage());
        deviceLastData.setCurrentTripMileage((double)obdStatDataTp.getCurrentTripMileage());
        int rpm = obdConditionProcoto.getRpm();
        deviceLastData.setRpm(rpm);

        log.debug("工况中的速度：{}，转速：{}",speed,rpm);

        // 判断汽车运行状态
        int carStatus = 0; // 0:熄火 转速无，车速无 1：运行 转速有，车速有 2：怠速 车速无，转速有
        if (rpm > 0 && speed > 0) {
            carStatus = 1;
        } else if (rpm > 0 && speed <= 0) {
            carStatus = 2;
        }

        deviceLastData.setCarStatus(carStatus);
        deviceLastDataService.lambdaUpdate().eq(DeviceLastData::getDeviceSn, deviceSn).update(deviceLastData);
    }
}
