package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.device.api.entity.DeviceCondition;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备工况检测
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Mapper
public interface DeviceConditionMapper extends BaseMapper<DeviceCondition> {

}
