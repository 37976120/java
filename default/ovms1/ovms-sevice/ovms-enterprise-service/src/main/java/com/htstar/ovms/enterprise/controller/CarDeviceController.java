package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.CarDevice;
import com.htstar.ovms.enterprise.service.CarDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;


/**
 * 车-设备 中间表
 *
 * @author lw
 * @date 2020-06-28 16:48:51
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cardevice" )
@Api(value = "cardevice", tags = "车-设备 中间表管理")
public class CarDeviceController {

    private final CarDeviceService carDeviceService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param carDevice 车-设备 中间表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getCarDevicePage(Page page, CarDevice carDevice) {
        return R.ok(carDeviceService.page(page, Wrappers.query(carDevice)));
    }


    /**
     * 通过id查询车-设备 中间表
     * @param carId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{carId}" )
    public R getById(@PathVariable("carId" ) Integer carId) {
        return R.ok(carDeviceService.getById(carId));
    }

    /**
     * 新增车-设备 中间表
     * @param carDevice 车-设备 中间表
     * @return R
     */
    @ApiOperation(value = "新增车-设备 中间表", notes = "新增车-设备 中间表")
    @SysLog("新增车-设备 中间表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('ovms-enterprise-service_cardevice_add')" )
    public R save(@RequestBody CarDevice carDevice) {
        return R.ok(carDeviceService.save(carDevice));
    }

    /**
     * 修改车-设备 中间表
     * @param carDevice 车-设备 中间表
     * @return R
     */
    @ApiOperation(value = "修改车-设备 中间表", notes = "修改车-设备 中间表")
    @SysLog("修改车-设备 中间表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('ovms-enterprise-service_cardevice_edit')" )
    public R updateById(@RequestBody CarDevice carDevice) {
        return R.ok(carDeviceService.updateById(carDevice));
    }

    /**
     * 通过id删除车-设备 中间表
     * @param carId id
     * @return R
     */
    @ApiOperation(value = "通过id删除车-设备 中间表", notes = "通过id删除车-设备 中间表")
    @SysLog("通过id删除车-设备 中间表" )
    @DeleteMapping("/{carId}" )
    @PreAuthorize("@pms.hasPermission('ovms-enterprise-service_cardevice_del')" )
    public R removeById(@PathVariable Integer carId) {
        return R.ok(carDeviceService.removeById(carId));
    }

}
