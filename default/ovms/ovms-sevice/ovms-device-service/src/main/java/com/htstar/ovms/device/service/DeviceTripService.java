package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.device.api.dto.*;
import com.htstar.ovms.device.api.entity.DeviceTrip;
import com.htstar.ovms.device.api.vo.*;
import com.htstar.ovms.device.protoco.ObdGpsDataTp;
import com.htstar.ovms.device.protoco.ObdStatDataTp;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 行程表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
public interface DeviceTripService extends IService<DeviceTrip> {

    /**
     * Description: 获取最后一次行程（更新行程使用，时间为点火时间）
     * Author: flr
     * Date: 2020-06-16
     * Company: 航通星空
     * Modified By:
     */
    DeviceTrip getLastTrip(String deviceSn, LocalDateTime lastAcconTime);

    /**
     * 根据设备编号查询行程信息
     * @return
     */
    DeviceTripCountTotalVO getTripInfoByDeviceSnPage(DeviceTripDTO deviceTripDTO);

    /**
     * Description: 处理协议行程 返回行程ID
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    Integer dealProcotoTrip(String deviceSn,
                            ObdGpsDataTp obdGpsDataTp,
                            ObdStatDataTp obdStatDataTp);

    /**
     * Description: 对行程的报警进行统计
     * Author: flr
     * Date: ${DATE}
     * Company: 航通星空
     * Modified By:
     */
    boolean updateAlarmCount(DeviceTrip deviceTrip);

    /**
     * 历史行程列表
     * @param tripHistoricalDTO
     * @return
     */
    Page<TripHistoricalVO> getTripHistoricalPage(TripHistoricalDTO tripHistoricalDTO);

    /**
     * 根据设备编号和时间获取行程轨迹
     * @param tripHistoricalDTO
     * @return
     */
    Page<DeviceTripVO> getDateTripPage(TripHistoricalDTO tripHistoricalDTO);

    /**
     * 统计 里程，油耗，耗时数据
     * @param deviceSn
     * @param monthTime
     * @return
     */
    StatisticsTripVO getStatisticsTrip(String deviceSn, String monthTime);

    /**
     * 导出轨迹列表
     * @param tripExportDTO
     * @return
     */
    List<TripAndCarInfoVO> exportTripByEtpAndLicCode(TripInfoExportDTO tripExportDTO);

    /**
     * 根据轨迹数据得出轨迹回放GPS数据
     * @param tripGpsDTO
     * @return
     */
    TripPlaybackVO getGPSDataByDeviceSn(TripGpsDTO tripGpsDTO);

    /**
     * 行程报表
     * @param tripReportFormsDTO
     * @return
     */
    Page<TripReportFormsVO> getTripReportForms(TripReportFormsDTO tripReportFormsDTO);

    /**
     * 导出列表
     * @param exportTripReportFormsDTO
     * @return
     */
    List<TripReportFormsVO> exportTripReportForms(ExportTripReportFormsDTO exportTripReportFormsDTO);
}
