package com.htstar.ovms.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.device.api.entity.CarFenceRemind;
import com.htstar.ovms.device.mapper.CarFenceRemindMapper;
import com.htstar.ovms.device.service.CarFenceRemindService;
import org.springframework.stereotype.Service;

/**
 * 车辆围栏提醒记录
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
@Service
public class CarFenceRemindServiceImpl extends ServiceImpl<CarFenceRemindMapper, CarFenceRemind> implements CarFenceRemindService {

    @Override
    public CarFenceRemind getLastRemindByTripId(int tripId) {
        return this.baseMapper.getLastRemindByTripId(tripId);
    }
}
