package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.device.api.dto.*;
import com.htstar.ovms.device.api.entity.DeviceAlarm;
import com.htstar.ovms.device.api.entity.DriverMapAreaCarInfo;
import com.htstar.ovms.device.api.vo.*;
import com.htstar.ovms.enterprise.api.vo.AlarmDataVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 设备警情
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
public interface DeviceAlarmService extends IService<DeviceAlarm> {

    /**
     * 根据设备编号查询行程信息
     * @param page
     * @param deviceSn
     * @param startTime
     * @param endTime
     * @param type 警情类型
     * @return
     */
    Page<DeviceAlarmVO> getAlarmInfoByDeviceSnPage(Page page, String deviceSn, String startTime, String endTime, Integer type);


    DeviceAlarm getEnt(String deviceSn, Long alarmNo, Integer alarmType);

    /**
     * 安防警报列表
     * @param carSecurityAlarmDTO
     * @return
     */
    Page<CarSecurityAlarmVO> getCarSecurityAlarmPage(CarSecurityAlarmDTO carSecurityAlarmDTO);

    /**
     * 根据类型和时间查看报警数据
     * @param carSecurityAlarmDTO
     * @return
     */
    Page<DeviceAlarm> getAlarmByTypeAndTimePage(CarSecurityAlarmDTO carSecurityAlarmDTO);

    /**
     * 查询监控列表
     * @param monitoringDTO
     * @return
     */
    Page<MonitoringVO> getMonitoringPage(MonitoringDTO monitoringDTO);
    /**
     * 查询监控列表不带分页
     * @param monitoringDTO
     * @return
     */
    public MonitoringLatLngCountVO getMonitorings(MonitoringLatLngDTO monitoringDTO);
    /**
     * 导出定位信息
     * @param monitoringDTO
     * @return
     */
    List<MonitoringVO> exportLocationInfo(ExportMonitoringDTO monitoringDTO);

    /**
     * 获取企业设备警情数据
     * @param etpId
     * @param dateTime
     * @return
     */
    AlarmDataVo getAlarmDataByEtpId(Integer etpId,String dateTime);

    /**
     * 查询地图
     * @param area
     * @return
     */
    List<AreaVo>  findList(AreaDTO area);

    /**
     * 查询地图接地式
     * @param
     * @return
     */
    List<AreaListVo>  findLists();
}
