package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.device.api.entity.DeviceLastData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备最新状态
 *
 * @author 范利瑞
 * @date 2020-06-20 16:42:12
 */
@Mapper
public interface DeviceLastDataMapper extends BaseMapper<DeviceLastData> {

    int updateOnline(@Param("deviceSn") String deviceSn, @Param("online") Integer online);
}
