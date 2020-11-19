package com.htstar.ovms.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.device.api.entity.DeviceAlarmGps;
import com.htstar.ovms.device.mapper.DeviceAlarmGpsMapper;
import com.htstar.ovms.device.protoco.GpsItemTp;
import com.htstar.ovms.device.protoco.ObdGpsDataTp;
import com.htstar.ovms.device.service.DeviceAlarmGpsService;
import com.htstar.ovms.device.service.GpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 警情相关的GPS数据
 *
 * @author flr
 * @date 2020-06-19 17:23:59
 */
@Service
public class DeviceAlarmGpsServiceImpl extends ServiceImpl<DeviceAlarmGpsMapper, DeviceAlarmGps> implements DeviceAlarmGpsService {

    @Autowired
    private GpsService gpsService;

    @Override
    public Integer saveGps(ObdGpsDataTp obdGpsDataTp) {
        if (obdGpsDataTp.getLastGpsItemTp() == null){
            return null;
        }

        DeviceAlarmGps gps = new DeviceAlarmGps();
        BeanUtil.copyProperties(obdGpsDataTp.getLastGpsItemTp(),gps);
        gps.setGpsAddr(gpsService.getGpsAddr(gps.getLat(),gps.getLng()));

        if (this.save(gps)){
            return gps.getId();
        }else {
            return null;
        }
    }


    @Override
    public String saveGpsAddr(ObdGpsDataTp obdGpsDataTp) {
        if (obdGpsDataTp.getLastGpsItemTp() == null){
            return null;
        }

        DeviceAlarmGps gps = new DeviceAlarmGps();
        BeanUtil.copyProperties(obdGpsDataTp.getLastGpsItemTp(),gps);
        gps.setGpsAddr(gpsService.getGpsAddr(gps.getLat(),gps.getLng()));
        return gps.getGpsAddr();
    }
}
