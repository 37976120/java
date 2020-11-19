package com.htstar.ovms.device.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.device.api.entity.CarFenceRemind;
import com.htstar.ovms.device.service.CarFenceRemindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 车辆围栏提醒记录
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/carfenceremind" )
@Api(value = "carfenceremind", tags = "车辆围栏提醒记录管理")
public class CarFenceRemindController {

    private final  CarFenceRemindService carFenceRemindService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param carFenceRemind 车辆围栏提醒记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getCarFenceRemindPage(Page page, CarFenceRemind carFenceRemind) {
        return R.ok(carFenceRemindService.page(page, Wrappers.query(carFenceRemind)));
    }


    /**
     * 通过id查询车辆围栏提醒记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(carFenceRemindService.getById(id));
    }

    /**
     * 新增车辆围栏提醒记录
     * @param carFenceRemind 车辆围栏提醒记录
     * @return R
     */
    @ApiOperation(value = "新增车辆围栏提醒记录", notes = "新增车辆围栏提醒记录")
    @SysLog("新增车辆围栏提醒记录" )
    @PostMapping
    public R save(@RequestBody CarFenceRemind carFenceRemind) {
        return R.ok(carFenceRemindService.save(carFenceRemind));
    }

    /**
     * 修改车辆围栏提醒记录
     * @param carFenceRemind 车辆围栏提醒记录
     * @return R
     */
    @ApiOperation(value = "修改车辆围栏提醒记录", notes = "修改车辆围栏提醒记录")
    @SysLog("修改车辆围栏提醒记录" )
    @PutMapping
    public R updateById(@RequestBody CarFenceRemind carFenceRemind) {
        return R.ok(carFenceRemindService.updateById(carFenceRemind));
    }

    /**
     * 通过id删除车辆围栏提醒记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除车辆围栏提醒记录", notes = "通过id删除车辆围栏提醒记录")
    @SysLog("通过id删除车辆围栏提醒记录" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(carFenceRemindService.removeById(id));
    }

}
