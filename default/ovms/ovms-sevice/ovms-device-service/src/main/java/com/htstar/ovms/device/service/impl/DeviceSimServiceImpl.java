package com.htstar.ovms.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.device.api.dto.DeviceDTO;
import com.htstar.ovms.device.api.dto.DeviceSimDTO;
import com.htstar.ovms.device.api.dto.DeviceSimExportDTO;
import com.htstar.ovms.device.api.entity.Device;
import com.htstar.ovms.device.api.entity.DeviceSim;
import com.htstar.ovms.device.mapper.DeviceSimMapper;
import com.htstar.ovms.device.service.DeviceService;
import com.htstar.ovms.device.service.DeviceSimService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备SIM卡表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Service
public class DeviceSimServiceImpl extends ServiceImpl<DeviceSimMapper, DeviceSim> implements DeviceSimService {

    @Autowired
    private DeviceService deviceService;

    @Override
    public Page<DeviceSim> getDeviceSimPage(DeviceSimDTO deviceSimDTO) {
        return baseMapper.getDeviceSimPage(deviceSimDTO);
    }

    @Override
    public List<DeviceSim> exportSimInfo(DeviceSimExportDTO deviceSimDTO) {
        List<Integer> list = new ArrayList<>(10);
        if(StringUtils.isNotBlank(deviceSimDTO.getIds())){
            String[] ids = deviceSimDTO.getIds().split(",");
            for (String id : ids) {
                list.add(Integer.parseInt(id));
            }
        }
        return baseMapper.exportSimInfo(deviceSimDTO,list);
    }



    @Override
    public R saveSim(DeviceSim deviceSim) {
        String msg = checkSIMAndDeviceSn(null, deviceSim.getSim(), deviceSim.getDeviceSn());
        if (StringUtils.isNotBlank(msg)) {
            return R.failed(msg);
        }
        return R.ok(baseMapper.insert(deviceSim));
    }

    @Override
    public R updateSim(DeviceSim deviceSim) {
        String msg = checkSIMAndDeviceSn(deviceSim.getId(), deviceSim.getSim(), deviceSim.getDeviceSn());
        if (StringUtils.isNotBlank(msg)) {
            return R.failed(msg);
        }
        return R.ok(baseMapper.updateById(deviceSim));
    }

    /**
     * 添加和修改检查数据是否正确
     *
     * @param simNo
     * @param deviceSn
     * @return
     */
    private String checkSIMAndDeviceSn(Integer simId, String simNo, String deviceSn) {
        String msg = null;
        Integer count = 0;
        if (null != simId) {
            count = baseMapper.selectCount(new QueryWrapper<DeviceSim>().
                    eq("sim", simNo).notIn("id", simId));
        } else {
            count = baseMapper.selectCount(new QueryWrapper<DeviceSim>().
                    eq("sim", simNo));
        }
        if (count != 0) {
            msg = "SIM卡已存在";
        }
        if (StringUtils.isNotBlank(deviceSn) && deviceService.count(new QueryWrapper<Device>().eq("device_sn", deviceSn)) == 0) {
            msg = "找不到这个设备，请重新输入";
        }
        return msg;
    }

}
