package com.htstar.ovms.device.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.common.security.annotation.Inner;
import com.htstar.ovms.device.api.entity.DeviceCommand;
import com.htstar.ovms.device.api.req.ObdSetGpsReq;
import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.service.DeviceCommandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Description: 设备指令下发
 * Author: flr
 * Date: Created in 2020/6/17
 * Company: 航通星空
 * Modified By:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/deviceCommand" )
@Api(value = "设备指令下发", tags = "设备指令下发")
public class DeviceCommandController {

    private final DeviceCommandService deviceCommandService;

    @ApiOperation(value = "设置OBD GPS", notes = "设置OBD GPS")
    @PostMapping("/setObdGps" )
    public R setObdWifi(@RequestBody ObdSetGpsReq req) {
        return deviceCommandService.setObdGps(req);
    }


    /**
     * Description: 网关处理命令结果
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    @PostMapping("/handleGatewayResult")
    @Inner
    void handleGatewayResult(@RequestBody DeviceCommand deviceCommand){
        if (null != deviceCommand.getId()){
            deviceCommandService.updateById(deviceCommand);
        }
    }

    /**
     * Description: 获取命令的网关处理结果
     * @param seq
     * @return
     */
    @GetMapping("/getGatewayStatus/{seq}")
    @Inner
    Integer getGatewayStatus(@PathVariable("seq")Long seq){
        return deviceCommandService.getGatewayStatusById(seq);
    }

    /**
     * 分页查询
     * @param page 分页对象
     * @param deviceCommand 设备指令持久化
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getDeviceCommandPage(Page page, DeviceCommand deviceCommand) {
        return R.ok(deviceCommandService.page(page, Wrappers.query(deviceCommand)));
    }


    /**
     * 通过id查询设备指令持久化
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(deviceCommandService.getById(id));
    }

    /**
     * 新增设备指令持久化
     * @param deviceCommand 设备指令持久化
     * @return R
     */
    @ApiOperation(value = "新增设备指令", notes = "新增设备指令")
    @SysLog("新增设备指令持久化" )
    @PostMapping
    public R save(@RequestBody DeviceCommand deviceCommand) {
        return R.ok(deviceCommandService.save(deviceCommand));
    }

    /**
     * 修改设备指令持久化
     * @param deviceCommand 设备指令持久化
     * @return R
     */
    @ApiOperation(value = "修改设备指令持久化", notes = "修改设备指令持久化")
    @SysLog("修改设备指令持久化" )
    @PutMapping
    public R updateById(@RequestBody DeviceCommand deviceCommand) {
        return R.ok(deviceCommandService.updateById(deviceCommand));
    }

    /**
     * 通过id删除设备指令持久化
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除设备指令持久化", notes = "通过id删除设备指令持久化")
    @SysLog("通过id删除设备指令持久化" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(deviceCommandService.removeById(id));
    }

}
