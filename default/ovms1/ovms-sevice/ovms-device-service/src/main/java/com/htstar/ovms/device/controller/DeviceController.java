package com.htstar.ovms.device.controller;


import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.device.api.dto.DeviceDTO;
import com.htstar.ovms.device.api.dto.DeviceExportDTO;
import com.htstar.ovms.device.api.entity.Device;
import com.htstar.ovms.device.api.vo.DeviceDataVO;
import com.htstar.ovms.device.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * 设备
 *
 * @author flr
 * @date 2020-06-09 11:25:24
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/device")
@Api(value = "device", tags = "设备管理")
public class DeviceController {

    private final DeviceService deviceService;

    @ApiOperation(value = "检查设备是否可以绑定", notes = "检查设备是否可以绑定")
    @GetMapping("/checkBinding/{deviceSn}")
    public R checkBinding(@PathVariable("deviceSn") String deviceSn){
        return deviceService.checkDeviceIsBinding(deviceSn);
    }

    /**
     * 分页查询
     *
     * @param deviceDTO 设备查询参数
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/page")
    public R<Page<DeviceDataVO>> getDevicePage(@RequestBody DeviceDTO deviceDTO) {
        return R.ok(deviceService.selectDevicePage(deviceDTO));
    }


    /**
     * 通过deviceSn查询设备
     *
     * @param deviceSn 设备编号
     * @return R
     */
    @ApiOperation(value = "通过deviceSn查询设备", notes = "通过deviceSn查询设备")
    @GetMapping("/{deviceSn}")
    public R getById(@PathVariable("deviceSn") String deviceSn) {
        return R.ok(deviceService.selectDeviceDataVOByDeviceSn(deviceSn));
    }

    /**
     * 新增设备
     *
     * @param device 设备
     * @return R
     */
    @ApiOperation(value = "新增设备", notes = "新增设备")
    @SysLog("新增设备")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('device_device_add')")
    public R save(@RequestBody Device device) {
        return deviceService.saveDevice(device);
    }

    /**
     * 编辑设备
     *
     * @param device 设备
     * @return R
     */
    @ApiOperation(value = "编辑设备", notes = "编辑设备")
    @SysLog("编辑设备")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('device_device_edit')")
    public R updateById(@RequestBody Device device) {
        return deviceService.updateDevice(device);
    }

    /**
     * 通过设备编号批量或单个删除设备
     *
     * @param deviceSns deviceSns
     * @return R
     */
    @ApiOperation(value = "通过设备编号批量或单个删除设备", notes = "通过设备编号批量或单个删除设备")
    @SysLog("通过设备编号批量或单个删除设备")
    @DeleteMapping("/removeByDeviceSns")
//    @PreAuthorize("@pms.hasPermission('device_device_del')")
    public R removeByDeviceSns(@RequestParam(value = "deviceSns") String deviceSns) {
        return deviceService.removeByDeviceSns(deviceSns);
    }

    /**
     * 通过deviceSns批量或单个解除设备绑定
     *
     * @param deviceSns
     * @param isClearAll 1 全部清除 默认不清除
     * @return
     */
    @ApiOperation(value = "通过deviceSns批量或单个解除设备绑定", notes = "通过deviceSns批量或单个解除设备绑定")
    @SysLog("通过deviceSns批量或单个解除设备绑定")
    @DeleteMapping("/removeBindingByDeviceSn")
    public R removeBindingByDeviceSn(@RequestParam(value = "deviceSns") String deviceSns,
                                     @RequestParam(value = "isClearAll", required = false) Integer isClearAll) {
        return R.ok(deviceService.removeBindingByDeviceSns(deviceSns, isClearAll));
    }


    /**
     * 下载导入模板
     */
    @ApiOperation(value = "下载导入模板", notes = "下载导入模板")
    @SysLog("下载导入模板")
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(@RequestParam(value = "templateName")String templateName, HttpServletResponse response) {
        OutputStream out = null;
        InputStream inputStream = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(URLDecoder.decode(
                    templateName, "UTF-8").getBytes("UTF-8"), "ISO8859-1")+ ".xlsx");
            out = response.getOutputStream();
            inputStream = this.getClass().getResourceAsStream("/template/" + templateName + ".xlsx");
            int b = 0;
            byte[] buffer = new byte[1024];
            while ((b = inputStream.read(buffer)) != -1) {
                // 4.写到输出流(out)中
                out.write(buffer, 0, b);
            }
            out.flush();
        } catch (IOException e) {
            log.error("下载导入模板失败", e);
        } finally {
            IoUtil.close(out);
            IoUtil.close(inputStream);
        }
    }

    /**
     * 导出设备信息
     *
     * @param deviceDTO
     * @param response
     */
    @ApiOperation(value = "导出设备信息", notes = "导出设备信息")
    @SysLog("导出设备信息")
    @PostMapping("/exportDeviceInfo")
    public void exportSimInfo(@RequestBody DeviceExportDTO deviceDTO, HttpServletResponse response) {
        List<DeviceDataVO> devices = deviceService.exportDeviceInfo(deviceDTO);
        if (CollectionUtils.isEmpty(devices)) {
            return;
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.renameSheet("设备信息");
        List<Map<String, Object>> rows = new ArrayList<>();
        for (DeviceDataVO device : devices) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("设备序列号(*)", device.getDeviceSn() == null ? "" : device.getDeviceSn());
            row.put("设备型号", device.getLable() == null ? "" : device.getLable());
            row.put("企业名称", device.getEtpName() == null ? "" : device.getEtpName());
            row.put("车牌号", device.getLicCode() == null ? "" : device.getLicCode());
            row.put("车型", device.getCarType() == null ? "" : device.getCarType());
            row.put("车架号", device.getFrameCode() == null ? "" : device.getFrameCode());
            row.put("绑定时间", device.getCreateTime() == null ? "" : device.getCreateTime());
            rows.add(row);
        }
        writer.write(rows, true);
        OutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(URLEncoder.encode(
                    "设备信息" + OvmDateUtil.getCstNowDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    , "UTF-8").getBytes("UTF-8"), "ISO8859-1") + ".xlsx");
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("设备信息导出失败", e);
        } finally {
            writer.close();
            IoUtil.close(out);
        }
    }


    /**
     * 设备迁移(迁移没有使用的设备到其他企业下面)
     */
    @ApiOperation(value = "设备迁移(迁移没有使用的设备到其他企业下面)", notes = "设备迁移(迁移没有使用的设备到其他企业下面)")
    @SysLog("设备迁移(迁移没有使用的设备到其他企业下面)")
    @PostMapping("/transferDevice")
    public R transferDevice(@RequestParam("etpId") Integer etpId,
                            @RequestParam(value = "file", required = false) MultipartFile file
            , @RequestParam(value = "deviceSns", required = false) String deviceSns) {
        boolean flag = false;
        List<Device> devices = null;
        OvmsUser user = SecurityUtils.getUser();
        if (file != null && StringUtils.isNotBlank(file.getOriginalFilename()) && file.getSize() != 0) {
            devices = com.htstar.ovms.common.excel.util.ExcelWriter.onlyOneHeadingImport(file, Device.class);
        } else {
           if(StringUtils.isNotBlank(deviceSns)){
               devices = deviceService.list(new QueryWrapper<Device>().in("device_sn", Arrays.asList(deviceSns.split(","))));
           }
        }
        if (!CollectionUtils.isEmpty(devices)) {
            for (int i = 0; i < devices.size(); i++) {
                Device device = devices.get(i);
                device.setEtpId(etpId);
                device.setUpdateTime(OvmDateUtil.getCstNow());
                device.setUserId(user.getId());
                if (StringUtils.isNotBlank(device.getDeviceSn())) {
                    int count = deviceService.count(new QueryWrapper<Device>().eq("device_sn", device.getDeviceSn()));
                    if (count == 0) {
                        return R.failed("第" + (i + 1) + "行设备编号不存在存在,这行迁移失败");
                    }
                    flag = deviceService.update(device, new UpdateWrapper<Device>().eq("device_sn", device.getDeviceSn()));
                } else {
                    return R.failed("第" + (i + 1) + "行设备编号为空,这行迁移失败");
                }
            }
        } else {
            return R.failed("设备编号为空，迁移失败");
        }
        return R.ok(flag);
    }

    /**
     * 导入设备信息
     *
     * @param file
     * @param productType 设备型号
     * @param etpId       企业Id
     * @return
     */
    @ApiOperation(value = "导入设备信息", notes = "导入设备信息")
    @SysLog("导入设备信息")
    @PostMapping("/importDeviceInfo")
    public R importSimInfo(@RequestParam(value = "file") MultipartFile file,
                           @RequestParam(value = "productType") Integer productType
            , @RequestParam("etpId") Integer etpId) {
        boolean flag = true;
        OvmsUser user = SecurityUtils.getUser();
        List<Device> devices = com.htstar.ovms.common.excel.util.ExcelWriter.onlyOneHeadingImport(file, Device.class);
        if (!CollectionUtils.isEmpty(devices)) {
            for (int i = 0; i < devices.size(); i++) {
                Device device = devices.get(i);
                if (StringUtils.isNotBlank(device.getDeviceSn())) {
                    int count = deviceService.count(new QueryWrapper<Device>().eq("device_sn", device.getDeviceSn()));
                    if (count != 0) {
                        return R.failed("第" + (i + 1) + "行设备编号已经存在,这行导入失败");
                    }
                    device.setProductType(productType);
                    device.setEtpId(etpId);
                    device.setUserId(user.getId());
                    flag = deviceService.save(device);
                } else {
                    return R.failed("第" + (i + 1) + "行设备编号为空,这行导入失败");
                }
            }
        } else {
            return R.failed("设备信息为空，导入失败");
        }
        return R.ok(flag);
    }
}
