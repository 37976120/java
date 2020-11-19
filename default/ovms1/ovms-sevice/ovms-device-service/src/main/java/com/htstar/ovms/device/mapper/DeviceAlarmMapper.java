package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.device.api.dto.*;
import com.htstar.ovms.device.api.entity.DeviceAlarm;
import com.htstar.ovms.device.api.entity.DriverMapAreaCarInfo;
import com.htstar.ovms.device.api.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 设备警情
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Mapper
public interface DeviceAlarmMapper extends BaseMapper<DeviceAlarm> {

    /**
     * 根据设备编号查询行程信息
     *
     * @param page
     * @param deviceSn
     * @param startTime
     * @param endTime
     * @param type      警情类型
     * @return
     */
    Page<DeviceAlarmVO> getAlarmInfoByDeviceSnPage(Page page, @Param("deviceSn") String deviceSn,
                                                   @Param("startTime") String startTime,
                                                   @Param("endTime") String endTime, @Param("type") Integer type);

    DeviceAlarm getEnt(@Param("deviceSn") String deviceSn, @Param("alarmNo") Long alarmNo, @Param("alarmType") Integer alarmType);

    /**
     * 安防警报列表
     *
     * @param carSecurityAlarmDTO
     * @return
     */
    Page<CarSecurityAlarmVO> getCarSecurityAlarmPage(@Param("query") CarSecurityAlarmDTO carSecurityAlarmDTO);

    /**
     * 根据设备编号获取最近一次检测时间
     */
    LocalDateTime getLastTestTime(@Param("deviceSn") String deviceSn);

    /**
     * 根据类型和时间查看报警数据
     * @param carSecurityAlarmDTO
     * @return
     */
    Page<DeviceAlarm> getAlarmByTypeAndTimePage(@Param("query") CarSecurityAlarmDTO carSecurityAlarmDTO);

    /**
     * 查询监控列表
     * @param monitoringDTO
     * @return
     */
    Page<MonitoringVO> getMonitoringPage(@Param("query") MonitoringDTO monitoringDTO);
    /**
     * 查询监控列表不带分页
     * @param monitoringDTO
     * @return
     */
    List<MonitoringLatLngVO> getMonitorings(@Param("query") MonitoringLatLngDTO monitoringDTO);
    /**
     * 导出定位信息
     * @param monitoringDTO
     * @return
     */
    List<MonitoringVO> exportLocationInfo(@Param("query") ExportMonitoringDTO monitoringDTO);

    /**
     * 获取警情数量
     * @param etpId
     * @param date
     * @return
     */
    List<DeviceAlarm> getAlarmDataByEtpId(@Param("etpId") Integer etpId, @Param("date") Date date);

    /**
     * 获取围栏报警信息
     * @param etpId
     * @param date
     * @return
     */
    Integer getFenceCount(@Param("etpId") Integer etpId, @Param("date") Date date);
    /**
     *  查询地图
     * @param area
     * @return
     */
    List<AreaVo> findList(@Param("area")AreaDTO area);

    /**
     *  查询地图阶梯式
     *
     *
     */
    @SqlParser(filter = true)
    List<AreaListVo> findLists();


    List<DeviceAlarm> getAlarmByTypeAndTimeList(@Param("query")CarSecurityAlarmListDTO carSecurityAlarmDTO);
}
