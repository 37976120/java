package com.htstar.ovms.device.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.common.security.annotation.Inner;
import com.htstar.ovms.device.api.entity.DeviceLastData;
import com.htstar.ovms.device.service.DeviceLastDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 设备最新状态
 *
 * @author 范利瑞
 * @date 2020-06-20 16:42:12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/devicelastdata" )
@Api(value = "devicelastdata", tags = "设备最新状态管理")
public class DeviceLastDataController {

    private final  DeviceLastDataService deviceLastDataService;

    /**
     * 由心跳监听发现的设备下线
     * @param deviceSn
     * @param online
     */
    @Inner
    @GetMapping("/heartBeatOnline/{deviceSn}/{online}")
    void heartBeatOnline(@PathVariable("deviceSn")String deviceSn, @PathVariable("online")Integer online){
        deviceLastDataService.updateOnline(deviceSn,online);
    }

    /**
     * 分页查询
     * @param page 分页对象
     * @param deviceLastData 设备最新状态
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getDeviceLastDataPage(Page page, DeviceLastData deviceLastData) {
        return R.ok(deviceLastDataService.page(page, Wrappers.query(deviceLastData)));
    }


    /**
     * 通过id查询设备最新状态
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(deviceLastDataService.getById(id));
    }

    /**
     * 新增设备最新状态
     * @param deviceLastData 设备最新状态
     * @return R
     */
    @ApiOperation(value = "新增设备最新状态", notes = "新增设备最新状态")
    @SysLog("新增设备最新状态" )
    @PostMapping
    public R save(@RequestBody DeviceLastData deviceLastData) {
        return R.ok(deviceLastDataService.save(deviceLastData));
    }

    /**
     * 修改设备最新状态
     * @param deviceLastData 设备最新状态
     * @return R
     */
    @ApiOperation(value = "修改设备最新状态", notes = "修改设备最新状态")
    @SysLog("修改设备最新状态" )
    @PutMapping
    public R updateById(@RequestBody DeviceLastData deviceLastData) {
        return R.ok(deviceLastDataService.updateById(deviceLastData));
    }

    /**
     * 通过id删除设备最新状态
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除设备最新状态", notes = "通过id删除设备最新状态")
    @SysLog("通过id删除设备最新状态" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(deviceLastDataService.removeById(id));
    }

}
