package com.htstar.ovms.device.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.device.api.dto.DeviceSimDTO;
import com.htstar.ovms.device.api.dto.DeviceSimExportDTO;
import com.htstar.ovms.device.api.entity.Device;
import com.htstar.ovms.device.api.entity.DeviceSim;
import com.htstar.ovms.device.service.DeviceService;
import com.htstar.ovms.device.service.DeviceSimService;
import com.htstar.ovms.device.util.ObdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * 设备SIM卡表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/devicesim")
@Api(value = "devicesim", tags = "设备SIM卡表管理")
public class DeviceSimController {

    private final DeviceSimService deviceSimService;

    /**
     * 分页查询
     *
     * @param deviceSimDTO 查询数据
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/page")
    public R<Page<DeviceSim>> getDeviceSimPage(@RequestBody DeviceSimDTO deviceSimDTO) {
        return R.ok(deviceSimService.getDeviceSimPage(deviceSimDTO));
    }


    /**
     * 通过id查询设备SIM卡表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(deviceSimService.getById(id));
    }

    /**
     * 新增设备SIM卡表
     *
     * @param deviceSim 设备SIM卡表
     * @return R
     */
    @ApiOperation(value = "新增设备SIM卡表", notes = "新增设备SIM卡表")
    @SysLog("新增设备SIM卡表")
    @PostMapping
//  @PreAuthorize("@pms.hasPermission('device_devicesim_add')" )
    public R save(@RequestBody DeviceSim deviceSim) {
        return deviceSimService.saveSim(deviceSim);
    }

    /**
     * 修改设备SIM卡表
     *
     * @param deviceSim 设备SIM卡表
     * @return R
     */
    @ApiOperation(value = "修改设备SIM卡表", notes = "修改设备SIM卡表")
    @SysLog("修改设备SIM卡表")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('device_devicesim_edit')" )
    public R updateById(@RequestBody DeviceSim deviceSim) {
        return R.ok(deviceSimService.updateSim(deviceSim));
    }



    /**
     * 通过ids批量删除设备SIM卡表
     *
     * @param ids sim卡
     * @return R
     */
    @ApiOperation(value = "通过ids批量删除设备SIM卡表", notes = "通过ids批量删除设备SIM卡表")
    @SysLog("通过ids批量删除设备SIM卡表")
    @DeleteMapping("/removeBathByIds")
//    @PreAuthorize("@pms.hasPermission('device_devicesim_del')")
    public R removeBathByIds(@RequestParam(value = "ids") String ids) {
        return R.ok(deviceSimService.removeByIds(Arrays.asList(ids.split(","))));
    }

    /**
     * 导出SIM卡信息
     */
    @ApiOperation(value = "导出SIM卡信息", notes = "导出SIM卡信息")
    @SysLog("导出SIM卡信息")
    @PostMapping("/exportSimInfo")
    public void exportSimInfo(@RequestBody(required = false)DeviceSimExportDTO deviceSimDTO, HttpServletResponse response) {
        List<DeviceSim> deviceSims = deviceSimService.exportSimInfo(deviceSimDTO);;
        if (CollectionUtils.isEmpty(deviceSims)) {
            return;
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.renameSheet("sim卡信息");
        ArrayList<Map<String, Object>> rows = new ArrayList<>();
        for (DeviceSim deviceSim : deviceSims) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("sim卡号(*)", deviceSim.getSim() == null ? "" : deviceSim.getSim());
            row.put("设备序列号", deviceSim.getDeviceSn() == null ? "" : deviceSim.getDeviceSn());
            row.put("启用时间", deviceSim.getStartTime() == null ? "" : deviceSim.getStartTime());
            row.put("到期时间", deviceSim.getEndTime() == null ? "" : deviceSim.getEndTime());
            row.put("导入/创建时间", deviceSim.getCreateTime() == null ? "" : deviceSim.getCreateTime());
            rows.add(row);
        }
        writer.write(rows, true);
        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    new String(URLEncoder.encode("SIM卡信息" + OvmDateUtil.getCstNowDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            , "UTF-8").getBytes("UTF-8"), "ISO8859-1") + ".xlsx");
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("SIM信息导出失败", e);
        } finally {
            writer.close();
            IoUtil.close(out);
        }

    }

    /**
     * 导入SIM卡信息
     */
    @ApiOperation(value = "导入SIM卡信息", notes = "导入SIM卡信息")
    @SysLog("导入SIM卡信息")
    @PostMapping("/importSimInfo")
    public R importSimInfo(@RequestParam(value = "file") MultipartFile file) {
        boolean flag = false;
        List<DeviceSim> deviceSims = com.htstar.ovms.common.excel.util.ExcelWriter.onlyOneHeadingImport(file, DeviceSim.class);
        if (!CollectionUtils.isEmpty(deviceSims)) {
            for (int i = 0; i < deviceSims.size(); i++) {
                DeviceSim deviceSim = deviceSims.get(i);
                if (StringUtils.isNotBlank(deviceSim.getSim())) {
                    int count = deviceSimService.count(new QueryWrapper<DeviceSim>().eq("sim", deviceSim.getSim()));
                    if (count != 0) {
                        return R.failed("第" + (i + 1) + "行SIM卡号已经存在,这行导入失败");
                    }
                    LocalDateTime cstNow = OvmDateUtil.getCstNow();
                    if (deviceSim.getStartTime() == null) {
                        deviceSim.setStartTime(cstNow);
                    }
                    if (deviceSim.getEndTime() == null) {
                        deviceSim.setEndTime(cstNow);
                    }
                    if (deviceSim.getCreateTime() == null) {
                        deviceSim.setCreateTime(cstNow);
                    }
                    flag = deviceSimService.save(deviceSim);
                } else {
                    return R.failed("第" + (i + 1) + "行SIM卡号为空,这行导入失败");
                }

            }
        } else {
            return R.failed("导入失败，导致原因可能是模板问题或者数据出错");
        }
        return R.ok(flag);
    }
}
