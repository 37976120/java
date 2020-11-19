package com.htstar.ovms.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.device.api.entity.DeviceLastData;
import com.htstar.ovms.device.mapper.DeviceLastDataMapper;
import com.htstar.ovms.device.service.DeviceLastDataService;
import org.springframework.stereotype.Service;

/**
 * 设备最新状态
 *
 * @author 范利瑞
 * @date 2020-06-20 16:42:12
 */
@Service
public class DeviceLastDataServiceImpl extends ServiceImpl<DeviceLastDataMapper, DeviceLastData> implements DeviceLastDataService {

    /**
     * 由心跳监听发现的设备下线
     * @param deviceSn
     * @param online
     */
    @Override
    public void updateOnline(String deviceSn, Integer online) {
        DeviceLastData updateEnt = new DeviceLastData();
        updateEnt.setDeviceSn(deviceSn);
        updateEnt.setOnline(online);
        boolean upstatus = this.lambdaUpdate().eq(DeviceLastData :: getDeviceSn,deviceSn).update(updateEnt);
        if (!upstatus){
            DeviceLastData deviceLastData = new DeviceLastData();
            deviceLastData.setDeviceSn(deviceSn);
            deviceLastData.setOnline(online);
            this.save(deviceLastData);
        }
    }
}
