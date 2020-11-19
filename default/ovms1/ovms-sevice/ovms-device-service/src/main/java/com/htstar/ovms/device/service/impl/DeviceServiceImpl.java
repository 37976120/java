package com.htstar.ovms.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.feign.EtpInfoFeign;
import com.htstar.ovms.admin.api.vo.EtpInfoSVo;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.device.api.dto.DeviceDTO;
import com.htstar.ovms.device.api.dto.DeviceExportDTO;
import com.htstar.ovms.device.api.entity.Device;
import com.htstar.ovms.device.api.entity.DeviceAlarm;
import com.htstar.ovms.device.api.entity.DeviceCondition;
import com.htstar.ovms.device.api.entity.DeviceSim;
import com.htstar.ovms.device.api.vo.DeviceDataVO;
import com.htstar.ovms.device.mapper.DeviceMapper;
import com.htstar.ovms.device.mongo.model.ObdGpsDataMG;
import com.htstar.ovms.device.service.DeviceAlarmService;
import com.htstar.ovms.device.service.DeviceConditionService;
import com.htstar.ovms.device.service.DeviceService;
import com.htstar.ovms.device.service.DeviceSimService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 设备
 *
 * @author flr
 * @date 2020-06-09 11:25:24
 */
@Service
@Slf4j
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {
    @Autowired
    private DeviceAlarmService deviceAlarmService;
    @Autowired
    private DeviceConditionService deviceConditionService;

    @Autowired
    private DeviceSimService deviceSimService;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    EtpInfoFeign etpInfoFeign;

    @Override
    //@Deprecated   标识这个方法将要改动
    public Page<DeviceDataVO> selectDevicePage(DeviceDTO deviceDTO) {
        getEtpId(deviceDTO);
        if(Objects.equals(deviceDTO.getEtpId(),null))deviceDTO.setEtpId(0);
        List<EtpInfoSVo> currentAndParents1 = etpInfoFeign.getCurrentAndParents1(deviceDTO.getEtpId());
        currentAndParents1.forEach(etpInfoSVo -> {
            deviceDTO.setEtpIds(etpInfoSVo.getIds());
        });
        return baseMapper.selectDevicePage(deviceDTO);
    }
//    @Override
//    public Page<DeviceDataVO> selectDevicePageS(DeviceDTO deviceDTO) {
//        OvmsUser user = SecurityUtils.getUser();
//        if(deviceDTO.getEtpId() == null)deviceDTO.setEtpId(0);
//        List<EtpInfoSVo> currentAndParents1 = etpInfoFeign.getCurrentAndParents1(deviceDTO.getEtpId());
//        currentAndParents1.forEach(etpInfoSVo -> {
//            deviceDTO.setEtpIds(etpInfoSVo.getIds());
//        });
//        return baseMapper.selectDevicePage(deviceDTO);
//    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public R removeByDeviceSns(String deviceSns) {
        boolean is_del = false;
        for (String deviceSn : deviceSns.split(",")) {
            try {
                boolean bind = baseMapper.IsBindByDeviceSn(deviceSn);
                if (bind) {
                    //提示
                    return R.failed("此设备在绑定中，不允许删除。设备编号为：" + deviceSn);
                }
                //删除
                is_del = remove(new QueryWrapper<Device>().eq("device_sn", deviceSn));
                DeviceSim sim = deviceSimService.getOne(new QueryWrapper<DeviceSim>().eq("device_sn", deviceSn));
                if(null !=sim){
                    sim.setDeviceSn(null);
                    deviceSimService.updateById(sim);
                }
            } catch (Exception e) {
                log.error("编号为" + deviceSn + "删除失败", e);
            }
        }
        if (!is_del) {
            R.failed("删除失败");
        }
        return R.ok(is_del);
    }

    @Override
    public DeviceDataVO selectDeviceDataVOByDeviceSn(String deviceSn) {
        return baseMapper.selectDeviceDataVOByDeviceSn(deviceSn);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeBindingByDeviceSns(String deviceSns, Integer isClearAll) {
        boolean removeBindingByDeviceSns = baseMapper.removeBindingByDeviceSns(Arrays.asList(deviceSns.split(",")));
        if (isClearAll != null && isClearAll == 1) {
            deviceAlarmService.remove(new QueryWrapper<DeviceAlarm>().in("device_sn", deviceSns));
            deviceConditionService.remove(new QueryWrapper<DeviceCondition>().in("device_sn", deviceSns));
            Query query = Query.query(Criteria.where("device_sn").in(deviceSns));
            mongoTemplate.remove(query, ObdGpsDataMG.class);
        }
        return removeBindingByDeviceSns;
    }

    /**
     * Description: 检测是否是允许连接的设备
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public Integer getAllowStatus(String deviceSn) {
        return baseMapper.getAllowStatus(deviceSn);
    }

    @Override
    public List<DeviceDataVO> exportDeviceInfo(DeviceExportDTO deviceDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (deviceDTO.getEtpId() == null && user != null) {
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                deviceDTO.setEtpId(user.getEtpId());
            }
        }
        List<String> list = null;
        if(StringUtils.isNotBlank(deviceDTO.getDeviceSns())){
            list = Arrays.asList(deviceDTO.getDeviceSns().split(","));
        }
        return baseMapper.exportDeviceInfo(deviceDTO,list);
    }

    @Override
    public List<DeviceDataVO> getDeviceVOByDeviceSns(String deviceSns) {
        List<DeviceDataVO> list = new ArrayList<>();
        for (String deviceSn : deviceSns.split(",")) {
            DeviceDataVO deviceDataVO = baseMapper.getDeviceVOByDeviceSns(deviceSn);
            if (deviceDataVO != null) {
                list.add(deviceDataVO);
            }
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R saveDevice(Device device) {
        if (checkDeviceSn(device)) return R.failed("此设备编号已存在");
        OvmsUser user = SecurityUtils.getUser();
        device.setUserId(user.getId());
        int insert = baseMapper.insert(device);
        if(StringUtils.isNotBlank(device.getSim())){
            updateDeviceSim(device);
        }
        return R.ok(insert);
    }

    /**
     * 跟新Sim卡
     * @param device
     */
    private void updateDeviceSim(Device device) {
        DeviceSim deviceSim = new DeviceSim();
        deviceSim.setId(Integer.parseInt(device.getSim()));
        deviceSim.setDeviceSn(device.getDeviceSn());
        deviceSimService.updateById(deviceSim);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R updateDevice(Device device) {
        if (!checkDeviceSn(device)) return R.failed("此设备编号不存在，修改失败");
        int update = baseMapper.updateById(device);
        if(StringUtils.isNotBlank(device.getSim())){
            DeviceSim sim = deviceSimService.getOne(new QueryWrapper<DeviceSim>().eq("device_sn", device.getDeviceSn()));
            if(null !=sim){
                sim.setDeviceSn(null);
                deviceSimService.updateById(sim);
            }
            updateDeviceSim(device);
        }
        return R.ok(update);
    }


    /**
     * 检查设备是否可以绑定
     *
     * @param deviceSn
     * @return
     */
    @Override
    public R checkDeviceIsBinding(String deviceSn) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        Device device = baseMapper.selectOne(new QueryWrapper<Device>().eq("device_sn", deviceSn)
                .eq("etp_id", etpId));
        if (device == null) {
            return R.failed("本企业没有此设备,绑定失败");
        }
        if (device.getDeviceStatus() == 1) {
            return R.failed("改设备已经停用,绑定失败");
        }
        return R.ok();
    }

    /**
     * 检查设备编号是否存在
     *
     * @param device
     * @return
     */
    private boolean checkDeviceSn(Device device) {
        if (device != null && StringUtils.isNotBlank(device.getDeviceSn())) {
            Integer count = baseMapper.selectCount(new QueryWrapper<Device>().eq("device_sn", device.getDeviceSn()));
            if (count != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取企业ID
     *
     * @param deviceDTO
     */
    private void getEtpId(DeviceDTO deviceDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (deviceDTO.getEtpId() == null && user != null) {
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                deviceDTO.setEtpId(user.getEtpId());
            }
        }
    }

    /**
     * 获取企业ID
     *
     * @param
     */
    public int getEtpIds(String deviceSn) {
        QueryWrapper<Device> wrapper = new QueryWrapper<>();
        wrapper.eq("device_sn",deviceSn);
        Device device = baseMapper.selectOne(wrapper);
        return device.getEtpId();

    }
    /**
     * 根据设备找车辆最终找到所属车辆的用户
     */
    @Override
    public int getuserId(String deviceSn){
        return baseMapper.getuserId(deviceSn);
    }

    /**
     * 根据设备找车辆
     */
    @Override
    public String getLicCode(String deviceSn){
        return baseMapper.getLicCode(deviceSn);
    }

    @Override
    public Integer getCarId(String deviceSn) {
        return baseMapper.getCarId(deviceSn);
    }

}
