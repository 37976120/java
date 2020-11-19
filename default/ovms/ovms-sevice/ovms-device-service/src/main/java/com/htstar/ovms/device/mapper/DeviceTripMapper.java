package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.device.api.dto.*;
import com.htstar.ovms.device.api.entity.DeviceTrip;
import com.htstar.ovms.device.api.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 行程表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Mapper
public interface DeviceTripMapper extends BaseMapper<DeviceTrip> {

    /**
     * Description: 获取最后一次行程（更新行程使用，时间为点火时间）
     * Author: flr
     * Date: 2020-06-16
     * Company: 航通星空
     * Modified By:
     */
    DeviceTrip getLastTrip(@Param("deviceSn") String deviceSn, @Param("staTime") LocalDateTime staTime);

    /**
     * 根据设备编号查询行程信息
     *
     * @return
     */
    Page<DeviceTripVO> getTripInfoByDeviceSnPage(@Param("query") DeviceTripDTO deviceTripDTO);
    /**
     * 统计该设备某天的统计 里程，油耗，耗时数据
     * @param
     * @return
     */
    DeviceTripCountTotalVO getTripInfoByDeviceSnTotal(@Param("query")DeviceTripTotalDTO deviceTripTotalDTO);
    /**
     * Description: 对行程的报警进行统计修改
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    void updateAlarmCount(DeviceTrip deviceTrip);

    /**
     * 历史行程列表
     * @param tripHistoricalDTO
     * @return
     */
    Page<TripHistoricalVO> getTripHistoricalPage(@Param("query") TripHistoricalDTO tripHistoricalDTO);

    /**
     * 根据设备编号和时间获取行程轨迹
     * @param tripHistoricalDTO
     * @return
     */
    Page<DeviceTripVO> getDateTripPage(@Param("query") TripHistoricalDTO tripHistoricalDTO);

    Integer getDateTripPages(@Param("deviceSn") String deviceSn,@Param("selectDate") Integer selectDate,@Param("dayTime") String dayTime );

    /**
     * 统计 里程，油耗，耗时数据
     * @param deviceSn
     * @return
     */
    StatisticsTripVO getStatisticsData(@Param("deviceSn") String deviceSn,@Param("monthTime") String monthTime);

    /**
     * 获取这个月 每天的里程数据
     * @param deviceSn
     * @param monthTime
     * @return
     */
    List<StatisticsMileageVO> getMontMileage(@Param("deviceSn") String deviceSn,@Param("monthTime") String monthTime);

    /**
     * 获取这个月 每天的耗时数据
     * @param deviceSn
     * @param monthTime
     * @return
     */
    List<StatisticsElapsedTimeVO> getMontElapsedTime(@Param("deviceSn") String deviceSn,@Param("monthTime") String monthTime);

    /**
     * 获取这个月 每天的油耗数据
     * @param deviceSn
     * @param monthTime
     * @return
     */
    List<StatisticsFuelConsumptionVO> getMontFuelConsumption(@Param("deviceSn") String deviceSn,@Param("monthTime") String monthTime);


    /**
     * 根据企业和车牌号查询行程轨迹
     * @param tripExportDTO
     * @return
     */
    List<TripAndCarInfoVO> exportTripByEtpAndLicCode(@Param("query") TripInfoExportDTO tripExportDTO);

    /**
     * 行驶报表
     * @param tripReportFormsDTO
     * @return
     */
    Page<TripReportFormsVO> getTripReportForms(@Param("query") TripReportFormsDTO tripReportFormsDTO);

    /**
     * 导出行驶报表
     * @param exportTripReportFormsDTO
     * @return
     */
    List<TripReportFormsVO> exportTripReportForms(@Param("query") ExportTripReportFormsDTO exportTripReportFormsDTO);


}
