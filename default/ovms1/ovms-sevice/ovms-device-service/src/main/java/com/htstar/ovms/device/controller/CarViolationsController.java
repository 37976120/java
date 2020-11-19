package com.htstar.ovms.device.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.entity.SysDictItem;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;

import com.htstar.ovms.device.api.dto.CarViolationsDTO;
import com.htstar.ovms.device.api.entity.CarViolations;
import com.htstar.ovms.device.api.entity.DeviceAlarm;
import com.htstar.ovms.device.api.vo.CarViolationsVO;
import com.htstar.ovms.device.service.CarViolationsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 18:39:11
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/carviolations" )
@Api(value = "carviolations", tags = "违规车辆管理")
public class CarViolationsController {

    private final CarViolationsService carViolationsService;

    /**
     * 分页查询
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/page" )
    public R<Page<CarViolationsVO>> getCarViolationsPage(@RequestBody CarViolationsDTO carViolationsDTO) {

        return R.ok(carViolationsService.getAllCarViolationsPage(carViolationsDTO));
    }

    /**
     * 导出
     * @return
     */
    @ApiOperation(value = "导出违规记录", notes = "导出违规记录")
    @PostMapping("/exportCarViolations" )
    public void exportCarViolationsVO(@RequestBody CarViolationsDTO carViolationsDTO, HttpServletResponse response) {
        List<CarViolationsVO> carViolationsVOS = carViolationsService.exportCarViolationsVO(carViolationsDTO);
        if (CollectionUtils.isEmpty(carViolationsVOS)) {
            return;
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.renameSheet("违规车辆");
        List<Map<String, Object>> rows = new ArrayList<>();
        for (CarViolationsVO carViolationsVO :  carViolationsVOS) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("车牌号码", carViolationsVO.getLicCode() == null ? "" : carViolationsVO.getLicCode());
            row.put("设备号", carViolationsVO.getDeviceSn() == null ? "" : carViolationsVO.getDeviceSn());
            row.put("发生时间", carViolationsVO.getStaTime() == null ? "" : carViolationsVO.getStaTime());
            row.put("违规发生地址", carViolationsVO.getStaAddr() == null ? "" : carViolationsVO.getStaAddr());
            row.put("违规结束地址", carViolationsVO.getEndAddr() == null ? "" : carViolationsVO.getEndAddr());
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
    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(carViolationsService.getById(id));
    }


    /**
     * 新增
     * @param carViolations 
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_carviolations_add')" )
    public R save(@RequestBody CarViolations carViolations) {
        return R.ok(carViolationsService.save(carViolations));
    }

    /**
     * 修改
     * @param carViolations 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_carviolations_edit')" )
    public R updateById(@RequestBody CarViolations carViolations) {
        return R.ok(carViolationsService.updateById(carViolations));
    }

    /**
     * 通过id删除
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_carviolations_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(carViolationsService.removeById(id));
    }

}
