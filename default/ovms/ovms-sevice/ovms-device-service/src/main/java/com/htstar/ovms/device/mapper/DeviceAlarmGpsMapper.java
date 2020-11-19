package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.device.api.entity.DeviceAlarmGps;
import org.apache.ibatis.annotations.Mapper;

/**
 * 警情相关的GPS数据
 *
 * @author flr
 * @date 2020-06-19 17:23:59
 */
@Mapper
public interface DeviceAlarmGpsMapper extends BaseMapper<DeviceAlarmGps> {

}
