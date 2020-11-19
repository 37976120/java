package com.htstar.ovms.device.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.device.api.dto.*;
import com.htstar.ovms.device.api.entity.DeviceTrip;
import com.htstar.ovms.device.api.vo.*;
import com.htstar.ovms.device.service.DeviceTripService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 行程表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/devicetrip")
@Api(value = "devicetrip", tags = "行程表管理")
public class DeviceTripController {

    private final DeviceTripService deviceTripService;

    /**
     * 通过设备编号查询行程信息
     * @Author HanGuji
     * @return
     */
    @ApiOperation(value = "通过设备编号查询行程信息", notes = "通过设备编号查询行程信息")
    @PostMapping("/page")
    public R<DeviceTripCountTotalVO> getDeviceTripPage(@RequestBody DeviceTripDTO deviceTripDTO) {
        return R.ok(deviceTripService.getTripInfoByDeviceSnPage(deviceTripDTO));
    }

    /**
     * app 历史行程记录
     */
    @ApiOperation(value = "历史行程记录列表", notes = "历史行程记录列表")
    @PostMapping("/getTripHistoricalPage")
    public R<Page<TripHistoricalVO>> getTripHistoricalPage(@RequestBody TripHistoricalDTO tripHistoricalDTO) {
        return R.ok(deviceTripService.getTripHistoricalPage(tripHistoricalDTO));
    }

    /**
     *  app 本日，本月，本年，按照天数查询行程轨迹
     * @param tripHistoricalDTO
     * @return
     */
    @ApiOperation(value = "本日，本月，本年，按照天数查询行程轨迹", notes = "本日，本月，本年，按照天数查询行程轨迹")
    @PostMapping("/getDateTripPage")
    public R getDateTripPage(@RequestBody TripHistoricalDTO tripHistoricalDTO) {
        return R.ok(deviceTripService.getDateTripPage(tripHistoricalDTO));
    }

    /**
     * 统计(月) 数据
     */
    @ApiOperation(value = "统计(月) 数据", notes = "统计(月) 数据")
    @GetMapping("/getStatisticsTrip")
    public R getStatisticsTrip(@RequestParam(value = "deviceSn")String deviceSn
            , @RequestParam(value = "monthTime")String monthTime) {
        return R.ok(deviceTripService.getStatisticsTrip(deviceSn,monthTime));
    }


    /**
     * 通过id查询行程表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(deviceTripService.getById(id));
    }

    /**
     * 新增行程表
     *
     * @param deviceTrip 行程表
     * @return R
     */
    @ApiOperation(value = "新增行程表", notes = "新增行程表")
    @SysLog("新增行程表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('device_devicetrip_add')")
    public R save(@RequestBody DeviceTrip deviceTrip) {
        return R.ok(deviceTripService.save(deviceTrip));
    }

    /**
     * 修改行程表
     *
     * @param deviceTrip 行程表
     * @return R
     */
    @ApiOperation(value = "修改行程表", notes = "修改行程表")
    @SysLog("修改行程表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('device_devicetrip_edit')")
    public R updateById(@RequestBody DeviceTrip deviceTrip) {
        return R.ok(deviceTripService.updateById(deviceTrip));
    }

    /**
     * 通过id删除行程表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除行程表", notes = "通过id删除行程表")
    @SysLog("通过id删除行程表")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('device_devicetrip_del')")
    public R removeById(@PathVariable Integer id) {
        return R.ok(deviceTripService.removeById(id));
    }


    /**
     * 根据轨迹数据得出轨迹回放GPS数据
     */
    @ApiOperation(value = "根据轨迹数据得出轨迹回放GPS数据", notes = "根据轨迹数据得出轨迹回放GPS数据")
    @GetMapping("/getGPSDataByDeviceSn")
    public R<TripPlaybackVO> getGPSDataByDeviceSn(TripGpsDTO tripGpsDTO) {
        return R.ok(deviceTripService.getGPSDataByDeviceSn(tripGpsDTO));
    }

    /**
     * 行驶报表
     */
    @ApiOperation(value = "行驶报表", notes = "行驶报表")
    @PostMapping("/getTripReportForms")
    public R getTripReportForms(@RequestBody TripReportFormsDTO tripReportFormsDTO) {
        return R.ok(deviceTripService.getTripReportForms(tripReportFormsDTO));
    }

    /**
     * 导出行程报表
     */
    @ApiOperation(value = "导出行程报表", notes = "导出行程报表")
    @PostMapping("/exportTripReportForms")
    public void exportTripReportForms(@RequestBody ExportTripReportFormsDTO exportTripReportFormsDTO,HttpServletResponse response) {
        List<TripReportFormsVO> reportFormsVOS = deviceTripService.exportTripReportForms(exportTripReportFormsDTO);
        if (CollectionUtils.isEmpty(reportFormsVOS)) {
            return;
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.renameSheet("行程报表");
        List<Map<String, Object>> rows = new ArrayList<>();
        for (TripReportFormsVO reportFormsVO : reportFormsVOS) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("车牌号码", reportFormsVO.getLicCode() == null ? "" : reportFormsVO.getLicCode());
            row.put("月初里程", reportFormsVO.getStartMonthMileage() == null ? "" : reportFormsVO.getStartMonthMileage());
            row.put("月末里程", reportFormsVO.getEndMonthMileage() == null ? "" : reportFormsVO.getEndMonthMileage());
            row.put("行驶里程", reportFormsVO.getMonthMileage() == null ? "" : reportFormsVO.getMonthMileage());
            row.put("月初油耗", reportFormsVO.getStartFuelConsumption() == null ? "" : reportFormsVO.getStartFuelConsumption());
            row.put("月末油耗", reportFormsVO.getEndFuelConsumption() == null ? "" : reportFormsVO.getEndFuelConsumption());
            row.put("月用油量", reportFormsVO.getMonthFuelConsumption() == null ? "" : reportFormsVO.getMonthFuelConsumption());
            row.put("月油耗/百公里", reportFormsVO.getMonthFuelOfKilometers() == null ? "" : reportFormsVO.getMonthFuelOfKilometers());
            rows.add(row);
        }
        getExportExcel(response, writer, rows, "行程报表", "行程报表导出失败");
    }

    /**
     * 导出模板
     * @param response
     * @param writer
     * @param rows
     * @param fileName
     * @param errorMsg
     */
    private void getExportExcel(HttpServletResponse response, ExcelWriter writer, List<Map<String, Object>> rows, String fileName, String errorMsg) {
        writer.write(rows, true);
        OutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(URLEncoder.encode(
                    fileName + OvmDateUtil.getCstNowDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    , "UTF-8").getBytes("UTF-8"), "ISO8859-1") + ".xlsx");
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            log.error(errorMsg, e);
        } finally {
            writer.close();
            IoUtil.close(out);
        }
    }

    /**
     * 导出 企业和车牌号查询行程轨迹
     */
    @ApiOperation(value = "导出 企业和车牌号查询行程轨迹", notes = "导出 企业和车牌号查询行程轨迹")
    @PostMapping("/exportTripByEtpAndLicCode")
    public void exportTripByEtpAndLicCode(@RequestBody TripInfoExportDTO tripExportDTO, HttpServletResponse response) {
        List<TripAndCarInfoVO> tripByEtpAndLicCode = deviceTripService.exportTripByEtpAndLicCode(tripExportDTO);
        if (CollectionUtils.isEmpty(tripByEtpAndLicCode)) {
            return;
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.renameSheet("轨迹信息");
        List<Map<String, Object>> rows = new ArrayList<>();
        for (TripAndCarInfoVO tripAndCarInfoVO : tripByEtpAndLicCode) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("车牌号码", tripAndCarInfoVO.getLicCode() == null ? "" : tripAndCarInfoVO.getLicCode());
            row.put("行程开始时间", tripAndCarInfoVO.getStaTime() == null ? "" : tripAndCarInfoVO.getStaTime());
            row.put("行程结束时间", tripAndCarInfoVO.getEndTime() == null ? "" : tripAndCarInfoVO.getEndTime());
            String[] lngAndLat = tripAndCarInfoVO.getEndLatlon().split(",");
            row.put("纬度", lngAndLat[0] == null ? "" : lngAndLat[0]);
            row.put("经度", lngAndLat[1] == null ? "" : lngAndLat[1]);
            rows.add(row);
        }
        getExportExcel(response, writer, rows, "轨迹信息", "企业和车牌号查询行程轨迹导出失败");
    }
}
