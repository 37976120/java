package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.device.api.dto.DeviceSimDTO;
import com.htstar.ovms.device.api.dto.DeviceSimExportDTO;
import com.htstar.ovms.device.api.entity.DeviceSim;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备SIM卡表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
public interface DeviceSimService extends IService<DeviceSim> {

    /**
     * 分页查询sim卡数据
     * @param deviceSimDTO
     * @return
     */
    Page<DeviceSim> getDeviceSimPage(DeviceSimDTO deviceSimDTO);


    /**
     * 导出查询数据
     * @param deviceSimDTO
     * @return
     */
    List<DeviceSim> exportSimInfo(DeviceSimExportDTO deviceSimDTO);

    /**
     * 保存
     * @param deviceSim
     * @return
     */
    R saveSim(DeviceSim deviceSim);

    /**
     * 修改
     * @param deviceSim
     * @return
     */
    R updateSim(DeviceSim deviceSim);
}
