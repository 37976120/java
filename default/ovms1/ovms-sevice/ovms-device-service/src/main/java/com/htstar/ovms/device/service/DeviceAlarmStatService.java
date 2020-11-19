package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.device.api.entity.DeviceAlarmStat;
import com.htstar.ovms.device.protoco.ObdStatDataTp;

/**
 * 警情相关的汽车状态
 *
 * @author flr
 * @date 2020-06-19 17:23:59
 */
public interface DeviceAlarmStatService extends IService<DeviceAlarmStat> {

    Integer saveStat(ObdStatDataTp obdStatDataTp);
}
