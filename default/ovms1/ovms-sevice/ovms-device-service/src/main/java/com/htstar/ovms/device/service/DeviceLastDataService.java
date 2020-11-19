package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.device.api.entity.DeviceLastData;

/**
 * 设备最新状态
 *
 * @author 范利瑞
 * @date 2020-06-20 16:42:12
 */
public interface DeviceLastDataService extends IService<DeviceLastData> {
    /**
     * 由心跳监听发现的设备下线
     * @param deviceSn
     * @param online
     */
    void updateOnline(String deviceSn, Integer online);

    DeviceLastData byCarId(String carId);
}
