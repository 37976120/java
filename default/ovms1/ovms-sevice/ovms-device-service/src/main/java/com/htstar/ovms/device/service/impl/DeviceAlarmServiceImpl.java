package com.htstar.ovms.device.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.feign.EtpInfoFeign;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.device.api.constant.AlarmTypeConstant;
import com.htstar.ovms.device.api.dto.*;
import com.htstar.ovms.device.api.entity.DeviceAlarm;
import com.htstar.ovms.device.api.entity.DriverMapAreaCarInfo;
import com.htstar.ovms.device.api.feign.DeviceGpsFeign;
import com.htstar.ovms.device.api.vo.*;
import com.htstar.ovms.device.mapper.DeviceAlarmMapper;
import com.htstar.ovms.device.service.DeviceAlarmService;
import com.htstar.ovms.device.service.GpsService;
import com.htstar.ovms.device.util.Cp;
import com.htstar.ovms.device.util.PositionUtils;
import com.htstar.ovms.enterprise.api.vo.AlarmDataVo;
import com.htstar.ovms.enterprise.api.vo.CarLocationsLatLngVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 设备警情
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Service
@Slf4j
public class DeviceAlarmServiceImpl extends ServiceImpl<DeviceAlarmMapper, DeviceAlarm> implements DeviceAlarmService {

    @Autowired
    private GpsService gpsService;

    @Autowired
    EtpInfoFeign etpInfoFeign;

    @Override
    public Page<DeviceAlarmVO> getAlarmInfoByDeviceSnPage(Page page, String deviceSn, String startTime, String endTime, Integer type) {
        return baseMapper.getAlarmInfoByDeviceSnPage(page, deviceSn, startTime, endTime, type);
    }

    @Override
    public DeviceAlarm getEnt(String deviceSn, Long alarmNo, Integer alarmType) {
        return this.baseMapper.getEnt(deviceSn, alarmNo, alarmType);
    }

    @Override
    public Page<CarSecurityAlarmVO> getCarSecurityAlarmPage(CarSecurityAlarmDTO carSecurityAlarmDTO) {
        Integer etpId = TenantContextHolder.getEtpId();
        carSecurityAlarmDTO.setEtpId(etpId);
        Page<CarSecurityAlarmVO> carSecurityAlarmPage = baseMapper.getCarSecurityAlarmPage(carSecurityAlarmDTO);
        for (CarSecurityAlarmVO record : carSecurityAlarmPage.getRecords()) {
            int unRead = 0, totalAlarm = 0;
            LocalDateTime DateTime = null;
            String deviceSn = record.getDeviceSn();
            if (StringUtils.isNotBlank(deviceSn)) {
                //unRead = count(new QueryWrapper<DeviceAlarm>().eq("device_sn", deviceSn).eq("is_read", "0").eq("is_del",0));
                totalAlarm = count(new QueryWrapper<DeviceAlarm>().eq("device_sn", deviceSn));
                DateTime = baseMapper.getLastTestTime(deviceSn);
            }
            //record.setUnRead(unRead);
            record.setTotalAlarm(totalAlarm);
            record.setLastDateTime(DateTime);
        }
        return carSecurityAlarmPage;
    }

    @Override
    public Page<DeviceAlarm> getAlarmByTypeAndTimePage(CarSecurityAlarmDTO carSecurityAlarmDTO) {
        getEtpId(carSecurityAlarmDTO);
        return baseMapper.getAlarmByTypeAndTimePage(carSecurityAlarmDTO);
    }

    @Override
    public List<DeviceAlarm> getAlarmByTypeAndTimeList(CarSecurityAlarmDTO carSecurityAlarmDTO) {
        CarSecurityAlarmListDTO cp = Cp.cp(carSecurityAlarmDTO, new CarSecurityAlarmListDTO());
        if (carSecurityAlarmDTO.getId() != null) {
            int[] objects = Arrays.stream(carSecurityAlarmDTO.getId().split(","))
                    .mapToInt(s -> Integer.parseInt(s)).toArray();
            cp.setId(objects);
        }
        getEtpId(cp);
        return baseMapper.getAlarmByTypeAndTimeList(cp);
    }

    /**
     * 获取企业id
     *
     * @param deviceDTO
     */
    private void getEtpId(CarSecurityAlarmDTO deviceDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (deviceDTO.getEtpId() == null && user != null) {
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                deviceDTO.setEtpId(user.getEtpId());
            }
        }
    }

    /**
     * 获取企业id
     *
     * @param deviceDTO
     */
    private void getEtpId(CarSecurityAlarmListDTO deviceDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (deviceDTO.getEtpId() == null && user != null) {
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                deviceDTO.setEtpId(user.getEtpId());
            }
        }
    }

    @Override
    public Page<MonitoringVO> getMonitoringPage(MonitoringDTO monitoringDTO) {
        OvmsUser userCurr = SecurityUtils.getUser();
        Integer deptId = userCurr.getDeptId();
        //获取其下所有企业
        R etpTreeNoDept = etpInfoFeign.getEtpTreeNoDeptHsl(deptId);
        List curEtp = (List) etpTreeNoDept.getData();
        Map o = (Map) curEtp.get(0);
        List ids = (List) o.get("ids");//得到该企业下所有子企业idList
        ids.add(deptId);
        monitoringDTO.setEtpIds(ids);
        //打开页面时未传EtpId。自动获取
//        if (CollectionUtils.isEmpty(monitoringDTO.getEtpIds())) {
//            OvmsUser user = SecurityUtils.getUser();
//            ArrayList<Integer> list = new ArrayList<>();
//            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
//                list.add(user.getEtpId());
//                monitoringDTO.setEtpIds(list);
//            }
//        }
        Page<MonitoringVO> page = baseMapper.getMonitoringPage(monitoringDTO);
        List<MonitoringVO> records = page.getRecords();
        for (MonitoringVO record : records) {
            //解析地址
            if (null != record.getLat() && null != record.getLng()) {
                String gpsAddr = gpsService.getGpsAddr(record.getLat(), record.getLng());
                record.setAddr(gpsAddr);
            }
            //解析方向
            if (null != record.getDirection()) {
                String deretion = PositionUtils.getDeretion(record.getDirection());
                record.setOrientation(deretion);
            }

        }
        return page;
    }

    @Override
    public MonitoringLatLngCountVO getMonitorings(MonitoringLatLngDTO monitoringDTO) {
        MonitoringLatLngCountVO vos = new MonitoringLatLngCountVO();
        int count1 = 0;
        int count2 = 0;
        if (CollectionUtils.isEmpty(monitoringDTO.getEtpIds())) {
            OvmsUser user = SecurityUtils.getUser();
            ArrayList<Integer> list = new ArrayList<>();
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                list.add(user.getEtpId());
                monitoringDTO.setEtpIds(list);
            }
        }
        List<MonitoringLatLngVO> list = baseMapper.getMonitorings(monitoringDTO);
        for (MonitoringLatLngVO record : list) {
            //解析地址
            if (null != record.getLat() && null != record.getLng()) {
                String gpsAddr = gpsService.getGpsAddr(record.getLat(), record.getLng());
                record.setAddr(gpsAddr);
            }
            //解析方向
            if (null != record.getDirection()) {
                String deretion = PositionUtils.getDeretion(record.getDirection());
                record.setOrientation(deretion);
            }

        }
        for (int i = 0; i < list.size(); i++) {
            MonitoringLatLngVO vo = list.get(i);
            if (vo.getOnline() != null && vo.getOnline() == 1) {
                vo.getOnline();
                count1++;

            }
            if (vo.getOnline() != null && vo.getOnline() == 0) {
                vo.getOnline();
                count2++;

            }

        }
        vos.setMonitoringLatLngVOS(list);
        vos.setCountCarStatus(count1);
        vos.setCountCarSends(count2);
        return vos;
    }

    @Override
    public List<MonitoringVO> exportLocationInfo(ExportMonitoringDTO monitoringDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (monitoringDTO.getEtpId() == -1 && user != null) {
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                monitoringDTO.setEtpId(user.getEtpId());
            }
        }
        return baseMapper.exportLocationInfo(monitoringDTO);
    }

    /**
     * 获取企业警情数据
     *
     * @param etpId
     * @param dateTime
     * @return
     */
    @Override
    public AlarmDataVo getAlarmDataByEtpId(Integer etpId, String dateTime) {
        Date date = DateUtil.parse(dateTime);
        AlarmDataVo vo = new AlarmDataVo();
        List<DeviceAlarm> list = baseMapper.getAlarmDataByEtpId(etpId, date);
//        baseMapper.selectList()
        if (CollUtil.isNotEmpty(list)) {
            for (DeviceAlarm deviceAlarm : list) {
                //数量
                Integer alarmCount = deviceAlarm.getAlarmCount();
                //类型
                Integer alarmType = deviceAlarm.getAlarmType();
                switch (alarmType) {
                    case AlarmTypeConstant.ACCELERATE://4
                        vo.setAccelerate(alarmCount);
                        break;
                    case AlarmTypeConstant.DECELERATE://5
                        vo.setDecelerate(alarmCount);
                        break;
                    case AlarmTypeConstant.TURN_AROUND://12
                        vo.setTurnAround(alarmCount);
                        break;
                    case AlarmTypeConstant.SPEEDING://1
                        vo.setSpeeding(alarmCount);
                        break;
                    case AlarmTypeConstant.PULL_OUT://14
                        vo.setPullOut(alarmCount);
                        break;
                    case AlarmTypeConstant.LOW_BATTERY://2
                        vo.setLowBattery(alarmCount);
                        break;
                    case AlarmTypeConstant.TOW://7
                        vo.setTow(alarmCount);
                        break;
                    case AlarmTypeConstant.IGNITION://22
                        vo.setIgnition(alarmCount);
                        break;
                    case AlarmTypeConstant.WATER_TEMP://3
                        vo.setWaterTemp(alarmCount);
                        break;
                    case AlarmTypeConstant.MALFUNCTION://24
                        vo.setMalfunction(alarmCount);
                        break;
                    case AlarmTypeConstant.COLLISION://17
                        vo.setCollision(alarmCount);
                        break;
                    case AlarmTypeConstant.FENCE://29
                        vo.setFence(alarmCount);
                        break;
                    default:
                        break;
                }

            }
        }
        return vo;
    }

    /**
     * 查询地图
     *
     * @param area
     * @return
     */
    @Override
    public List<AreaVo> findList(AreaDTO area) {
        return baseMapper.findList(area);
    }

    /**
     * 查询地图阶梯式
     *
     * @param
     * @return
     */
    @Override
    public List<AreaListVo> findLists() {
        return baseMapper.findLists();
    }


}
