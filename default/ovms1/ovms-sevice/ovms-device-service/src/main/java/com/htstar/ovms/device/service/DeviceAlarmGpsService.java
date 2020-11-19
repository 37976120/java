package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.device.api.entity.DeviceAlarmGps;
import com.htstar.ovms.device.protoco.ObdGpsDataTp;

/**
 * 警情相关的GPS数据
 *
 * @author flr
 * @date 2020-06-19 17:23:59
 */
public interface DeviceAlarmGpsService extends IService<DeviceAlarmGps> {

    /**
     * 通过协议入库
     * @param obdGpsDataTp
     * @return
     */
    Integer saveGps(ObdGpsDataTp obdGpsDataTp);

    /**
     * 警报地址
     * @param obdGpsDataTp
     * @return
     */
    String saveGpsAddr(ObdGpsDataTp obdGpsDataTp);



}
