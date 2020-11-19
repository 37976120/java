package com.htstar.ovms.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.device.api.entity.DeviceAlarmStat;
import com.htstar.ovms.device.mapper.DeviceAlarmStatMapper;
import com.htstar.ovms.device.protoco.ObdStatDataTp;
import com.htstar.ovms.device.service.DeviceAlarmStatService;
import org.springframework.stereotype.Service;

/**
 * 警情相关的汽车状态
 *
 * @author flr
 * @date 2020-06-19 17:23:59
 */
@Service
public class DeviceAlarmStatServiceImpl extends ServiceImpl<DeviceAlarmStatMapper, DeviceAlarmStat> implements DeviceAlarmStatService {

    @Override
    public Integer saveStat(ObdStatDataTp obdStatDataTp) {
        if (null == obdStatDataTp){
            return null;
        }
        DeviceAlarmStat deviceAlarmStat = new DeviceAlarmStat();
        BeanUtil.copyProperties(obdStatDataTp,deviceAlarmStat);
        deviceAlarmStat.setVstate(JSON.toJSONString(obdStatDataTp.getObdVStateTp()));
        deviceAlarmStat.setFireState(obdStatDataTp.getObdVStateTp().getFireState());
        if (this.save(deviceAlarmStat)){
            return deviceAlarmStat.getId();
        }else {
            return null;
        }
    }
}
