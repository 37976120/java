package com.htstar.ovms.device.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.device.api.dto.*;
import com.htstar.ovms.device.api.entity.DeviceAlarm;
import com.htstar.ovms.device.api.entity.DriverMapAreaCarInfo;
import com.htstar.ovms.device.api.vo.*;
import com.htstar.ovms.device.service.DeviceAlarmService;
import com.htstar.ovms.enterprise.api.entity.CarInfo;
import com.htstar.ovms.enterprise.api.vo.AlarmDataVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * 设备警情
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/devicealarm" )
@Api(value = "devicealarm", tags = "设备警情管理")
public class DeviceAlarmController {

    private final  DeviceAlarmService deviceAlarmService;

    /**
     * 通过设备编号查询警情信息
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/page" )
    public R<Page<DeviceAlarmVO>> getDeviceAlarmPage(@RequestParam(value = "page") Page page,
                                                        @RequestParam("deviceSn") String deviceSn,
                                                        @RequestParam(value = "startTime", required = false) String startTime
            , @RequestParam(value = "endTime", required = false) String endTime
            , @RequestParam(value = "type", required = false) Integer type) {
        return R.ok(deviceAlarmService.getAlarmInfoByDeviceSnPage(page, deviceSn,startTime,endTime,type));
    }


    /**
     * 通过id查询设备警情
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(deviceAlarmService.getById(id));
    }

    /**
     * 新增设备警情
     * @param deviceAlarm 设备警情
     * @return R
     */
        @ApiOperation(value = "新增设备警情", notes = "新增设备警情")
    @SysLog("新增设备警情" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('device_devicealarm_add')" )
    public R save(@RequestBody DeviceAlarm deviceAlarm) {

        return R.ok(deviceAlarmService.save(deviceAlarm));
    }

    /**
     * 修改设备警情
     * @param deviceAlarm 设备警情
     * @return R
     */
    @ApiOperation(value = "修改设备警情", notes = "修改设备警情")
    @SysLog("修改设备警情" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('device_devicealarm_edit')" )
    public R updateById(@RequestBody DeviceAlarm deviceAlarm) {
        return R.ok(deviceAlarmService.updateById(deviceAlarm));
    }

    /**
     * 通过id 逻辑删除设备警情
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id 逻辑删除设备警情", notes = "通过id 逻辑删除设备警情")
    @SysLog("通过id 逻辑删除设备警情" )
    @DeleteMapping("/{id}" )
//    @PreAuthorize("@pms.hasPermission('device_devicealarm_del')" )
    public R removeById(@PathVariable(value = "id") Integer id) {
        DeviceAlarm deviceAlarm = new DeviceAlarm();
        deviceAlarm.setId(id);
        deviceAlarm.setIsDel(1);
        return R.ok(deviceAlarmService.updateById(deviceAlarm));
    }

    /**
     * app 安防报警
     */
    @ApiOperation(value = "安防报警", notes = "安防报警")
    @PostMapping("/getCarSecurityAlarmPage" )
    public R<Page<CarSecurityAlarmVO>> getCarSecurityAlarmPage(@RequestBody CarSecurityAlarmDTO carSecurityAlarmDTO){
        return R.ok(deviceAlarmService.getCarSecurityAlarmPage(carSecurityAlarmDTO));
    }

    /**
     * 根据类型和时间查看报警数据
     */
    @ApiOperation(value = "根据类型和时间查看报警数据", notes = "根据类型和时间查看报警数据")
    @PostMapping("/getAlarmByTypeAndTimePage" )
    public R<Page<DeviceAlarm>> getAlarmByTypeAndTimePage(@RequestBody CarSecurityAlarmDTO carSecurityAlarmDTO){

        return R.ok(deviceAlarmService.getAlarmByTypeAndTimePage(carSecurityAlarmDTO));
    }

    /**
     * 查询监控和管理员后台定位信息列表
     */
    @ApiOperation(value = "查询监控和管理员后台定位信息列表", notes = "查询监控和管理员后台定位信息列表")
    @PostMapping("/getMonitoringPage")
    public R<Page<MonitoringVO>> getMonitoringPage(@RequestBody MonitoringDTO monitoringDTO){
        log.info("属性{}",monitoringDTO);
        return R.ok(deviceAlarmService.getMonitoringPage(monitoringDTO));
    }
    /**
     * 查询监控和管理员后台定位信息列表,不带分页
     */
    @ApiOperation(value = "查询监控和管理员后台定位信息列表,不带分页", notes = "查询监控和管理员后台定位信息列表,不带分页")
    @PostMapping("/getMonitorings")
    public R getMonitorings(@RequestBody MonitoringLatLngDTO monitoringDTO){
        return R.ok(deviceAlarmService.getMonitorings(monitoringDTO));
    }
    /**
     * 查询地图，根据传入区域代码和类型查询子区域
     */
    @ApiOperation(value = "查询地图，根据传入区域代码和类型查询子区域", notes = "查询地图，根据传入区域代码和类型查询子区域")
    @PostMapping("/arealistData")
    public R  arealistData(@RequestBody AreaDTO areaDTO) {
        return R.ok(deviceAlarmService.findList(areaDTO));
    }

    /**
     * 查询地图，根据传入区域代码和类型查询子区域阶梯式
     */
    @ApiOperation(value = "查询地图，根据传入区域代码和类型查询子区域区域阶梯式", notes = "查询地图，根据传入区域代码和类型查询子区域区域阶梯式")
    @GetMapping("/arealistDataPage")
    public R<List<AreaListVo>>  arealistDatas() {
        return R.ok(deviceAlarmService.findLists());
    }

    /**
     *
     * 后台定位信息导出
     */
    @ApiOperation(value = "后台定位信息导出", notes = "后台定位信息导出")
    @PostMapping("/exportLocationInfo" )
    public void exportLocationInfo(@RequestBody(required=false) ExportMonitoringDTO monitoringDTO, HttpServletResponse response){
        List<MonitoringVO> monitoringVOS = null;
        monitoringVOS = deviceAlarmService.exportLocationInfo(monitoringDTO);

        if (CollectionUtils.isEmpty(monitoringVOS)) {
            return;
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.renameSheet("设备信息");
        List<Map<String, Object>> rows = new ArrayList<>();
        for (MonitoringVO monitoringVO : monitoringVOS) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("车辆所属企业", monitoringVO.getEtpName() == null ? "" : monitoringVO.getEtpName());
            row.put("车牌号码", monitoringVO.getLicCode() == null ? "" : monitoringVO.getLicCode());
            row.put("经度", monitoringVO.getLng() == null ? "" : monitoringVO.getLng());
            row.put("纬度", monitoringVO.getLat() == null ? "" : monitoringVO.getLat());
            row.put("状态", monitoringVO.getOnline() == null ? "" : monitoringVO.getOnline()==1?"在线":"离线");
            row.put("定位时间", monitoringVO.getGpsTime() == null ? "" : monitoringVO.getGpsTime());
            row.put("通信时间", monitoringVO.getUpdateTime() == null ? "" : monitoringVO.getUpdateTime());
            rows.add(row);
        }
        writer.write(rows, true);
        OutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(URLEncoder.encode(
                    "定位信息" + OvmDateUtil.getCstNowDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    , "UTF-8").getBytes("UTF-8"), "ISO8859-1") + ".xlsx");
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("定位信息导出失败", e);
        } finally {
            writer.close();
            IoUtil.close(out);
        }
        return;
    }


    @ApiOperation(value = "企业警情数据统计", notes = "企业警情数据统计")
    @GetMapping("/getAlarmData/{etpId}/{date}")
    public R<AlarmDataVo > getAlarmData(@PathVariable("etpId")Integer etpId ,@PathVariable("date")  String date){
        return R.ok(deviceAlarmService.getAlarmDataByEtpId(etpId,date));
    }



}
