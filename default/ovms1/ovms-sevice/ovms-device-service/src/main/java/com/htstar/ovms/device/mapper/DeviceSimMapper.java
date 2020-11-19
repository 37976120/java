package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.device.api.dto.DeviceSimDTO;
import com.htstar.ovms.device.api.dto.DeviceSimExportDTO;
import com.htstar.ovms.device.api.entity.DeviceSim;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备SIM卡表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Mapper
public interface DeviceSimMapper extends BaseMapper<DeviceSim> {


    /**
     * 分页查询sim卡数据
     * @param deviceSimDTO
     * @return
     */
    Page<DeviceSim> getDeviceSimPage(@Param("query") DeviceSimDTO deviceSimDTO);

    /**
     * 导出查询数据
     * @param deviceSimDTO
     * @return
     */
    @SqlParser(filter = true)
    List<DeviceSim> exportSimInfo(@Param("query") DeviceSimExportDTO deviceSimDTO,@Param("list") List<Integer> list);
}
