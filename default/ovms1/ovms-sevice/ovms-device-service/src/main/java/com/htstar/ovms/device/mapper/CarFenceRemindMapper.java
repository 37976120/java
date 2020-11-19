package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.device.api.entity.CarFenceRemind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 车辆围栏提醒记录
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
@Mapper
public interface CarFenceRemindMapper extends BaseMapper<CarFenceRemind> {

    CarFenceRemind getLastRemindByTripId(@Param("tripId") int tripId);
}
