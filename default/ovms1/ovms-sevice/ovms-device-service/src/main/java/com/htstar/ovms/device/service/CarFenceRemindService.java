package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.device.api.entity.CarFenceRemind;

/**
 * 车辆围栏提醒记录
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
public interface CarFenceRemindService extends IService<CarFenceRemind> {

    CarFenceRemind getLastRemindByTripId(int tripId);
}
