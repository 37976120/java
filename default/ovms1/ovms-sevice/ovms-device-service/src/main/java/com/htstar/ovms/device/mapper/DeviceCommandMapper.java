package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.device.api.entity.DeviceCommand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备指令持久化
 *
 * @author flr
 * @date 2020-06-18 11:26:14
 */
@Mapper
public interface DeviceCommandMapper extends BaseMapper<DeviceCommand> {

    /**
     * 获取当前设备最大指令序号
     * @param deviceSn
     * @return
     */
    Long getMaxProtocoSeq(@Param("deviceSn") String deviceSn);

    Integer getGatewayStatusById(@Param("id") Long id);

    DeviceCommand getEnt(@Param("deviceSn") String deviceSn, @Param("protocoSeq") int protocoSeq);
}
