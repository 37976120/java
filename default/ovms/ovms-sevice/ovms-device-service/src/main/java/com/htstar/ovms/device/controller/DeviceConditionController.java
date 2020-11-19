package com.htstar.ovms.device.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.device.api.entity.DeviceCondition;
import com.htstar.ovms.device.service.DeviceConditionService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 设备工况检测
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/devicecondition" )
@Api(value = "devicecondition", tags = "设备工况检测管理")
public class DeviceConditionController {

    private final  DeviceConditionService deviceConditionService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param deviceCondition 设备工况检测
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getDeviceConditionPage(Page page, DeviceCondition deviceCondition) {
        return R.ok(deviceConditionService.page(page, Wrappers.query(deviceCondition)));
    }


    /**
     * 通过id查询设备工况检测
     * @param deviceSn id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{deviceSn}" )
    public R getById(@PathVariable("deviceSn" ) String deviceSn) {
        return R.ok(deviceConditionService.getById(deviceSn));
    }

    /**
     * 新增设备工况检测
     * @param deviceCondition 设备工况检测
     * @return R
     */
    @ApiOperation(value = "新增设备工况检测", notes = "新增设备工况检测")
    @SysLog("新增设备工况检测" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('device_devicecondition_add')" )
    public R save(@RequestBody DeviceCondition deviceCondition) {
        return R.ok(deviceConditionService.save(deviceCondition));
    }

    /**
     * 修改设备工况检测
     * @param deviceCondition 设备工况检测
     * @return R
     */
    @ApiOperation(value = "修改设备工况检测", notes = "修改设备工况检测")
    @SysLog("修改设备工况检测" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('device_devicecondition_edit')" )
    public R updateById(@RequestBody DeviceCondition deviceCondition) {
        return R.ok(deviceConditionService.updateById(deviceCondition));
    }

    /**
     * 通过id删除设备工况检测
     * @param deviceSn id
     * @return R
     */
    @ApiOperation(value = "通过id删除设备工况检测", notes = "通过id删除设备工况检测")
    @SysLog("通过id删除设备工况检测" )
    @DeleteMapping("/{deviceSn}" )
    @PreAuthorize("@pms.hasPermission('device_devicecondition_del')" )
    public R removeById(@PathVariable String deviceSn) {
        return R.ok(deviceConditionService.removeById(deviceSn));
    }

}
