package com.htstar.ovms.device.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.vo.EtpInfoVO;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.device.api.constant.AlarmTypeConstant;
import com.htstar.ovms.device.api.dto.AreaDTO;
import com.htstar.ovms.device.api.dto.CarSecurityAlarmDTO;
import com.htstar.ovms.device.api.dto.ExportMonitoringDTO;
import com.htstar.ovms.device.api.dto.MonitoringDTO;
import com.htstar.ovms.device.api.entity.DeviceAlarm;
import com.htstar.ovms.device.api.entity.DriverMapAreaCarInfo;
import com.htstar.ovms.device.api.vo.*;
import com.htstar.ovms.device.mapper.DeviceAlarmMapper;
import com.htstar.ovms.device.mapper.DeviceMapAreaCarInfoMapper;
import com.htstar.ovms.device.service.DeviceAlarmService;
import com.htstar.ovms.device.service.DeviceMapAreaCarInfoService;
import com.htstar.ovms.device.service.GpsService;
import com.htstar.ovms.device.util.PositionUtils;
import com.htstar.ovms.enterprise.api.entity.CarInfo;
import com.htstar.ovms.enterprise.api.feign.CarInfoFeign;
import com.htstar.ovms.enterprise.api.vo.AlarmDataVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 地图车辆关联信息
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Service
@Slf4j
public class DeviceMapAreaCarInfoServiceImpl extends ServiceImpl<DeviceMapAreaCarInfoMapper, DriverMapAreaCarInfo> implements DeviceMapAreaCarInfoService {


    /**
     * 新增地图车辆关联信息
     * @param driverMapAreaCarInfo
     * @return
     */
    @Override
    public R saveBaseMapAreaInfo(DriverMapAreaCarInfo driverMapAreaCarInfo) {
        return R.ok(  baseMapper.insert(driverMapAreaCarInfo));
    }

    /**
     * 根据车辆id查询所属地图标签
     * @param mapCarInfoId
     * @return
     */
    @Override
    public String getByMapCarInfoId(Integer mapCarInfoId) {
        return baseMapper.getByMapCarInfoId(mapCarInfoId);
    }


}
