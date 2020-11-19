package com.htstar.ovms.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.device.api.entity.DriverMapAreaCarInfo;
import com.htstar.ovms.device.service.DeviceMapAreaCarInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/driverArea" )
@Api(value = "车辆地图关联标签", tags = "车辆地图关联标签")
public class DriverMapSysAreaController {

    private final DeviceMapAreaCarInfoService deviceMapAreaCarInfoService;

    @ApiOperation(value = "新增地图车辆关联信息 ", notes = "新增地图车辆关联信息")
    @SysLog("新增地图车辆关联信息")
    @PostMapping("/baseMapAreaInfo")
    public R saveBaseMapAreaInfo(@RequestBody DriverMapAreaCarInfo driverMapAreaCarInfo) {
        return deviceMapAreaCarInfoService.saveBaseMapAreaInfo(driverMapAreaCarInfo);
    }

    @ApiOperation(value = "根据车辆id修改地图车辆关联信息 ", notes = "根据车辆id修改地图车辆关联信息")
    @SysLog("根据车辆id修改地图车辆关联信息")
    @PostMapping("/baseUpdateMapAreaInfo")
    public R updateBaseMapAreaInfo(@RequestBody DriverMapAreaCarInfo driverMapAreaCarInfo) {
        UpdateWrapper<DriverMapAreaCarInfo> wrapper = new UpdateWrapper<>();
                    if(driverMapAreaCarInfo.getMapAreaCode1() != null
                            && driverMapAreaCarInfo.getMapAreaCode2() == null
                            && driverMapAreaCarInfo.getMapAreaCode3() == null ){
                        wrapper.set("map_area_code2",null);
                        wrapper.set("map_area_code3",null);
                    }
                    if(driverMapAreaCarInfo.getMapAreaCode1() != null
                            && driverMapAreaCarInfo.getMapAreaCode2() != null
                            && driverMapAreaCarInfo.getMapAreaCode3() == null ){
                        wrapper.set("map_area_code3",null);
                    }

        wrapper.eq("map_car_info_id",driverMapAreaCarInfo.getMapCarInfoId());
        return  R.ok(deviceMapAreaCarInfoService.update(driverMapAreaCarInfo,wrapper));
    }

    @ApiOperation(value = "根据车辆id查询当前车辆地图标签全部字段 ", notes = "根据车辆id查询当前车辆地图标签全部字段")
    @SysLog("根据车辆id查询当前车辆地图标签全部字段")
    @GetMapping("/getByCarInfoId/{mapCarInfoId}")
    public DriverMapAreaCarInfo getBymMapCarInfoId(@PathVariable("mapCarInfoId") Integer mapCarInfoId) {
        QueryWrapper<DriverMapAreaCarInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("map_car_info_id",mapCarInfoId);
        DriverMapAreaCarInfo one = deviceMapAreaCarInfoService.getOne(wrapper);
        return  one;

    }

    @ApiOperation(value = "根据车辆id删除当前车辆地图标签 ", notes = "根据车辆id删除当前车辆地图标签")
    @SysLog("根据车辆id删除当前车辆地图标签")
    @GetMapping("/removeByMapCarInfoId/{mapCarInfoId}")
    public boolean removeByMapCarInfoId(@PathVariable("mapCarInfoId") Integer mapCarInfoId) {
        QueryWrapper<DriverMapAreaCarInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("map_car_info_id",mapCarInfoId);

        return   deviceMapAreaCarInfoService.remove(wrapper);

    }

    @ApiOperation(value = "根据车辆id查询所属地图标签 ", notes = "根据车辆id查询所属地图标签")
    @SysLog("根据车辆id查询所属地图标签")
    @GetMapping("/getByMapCarInfoId/{mapCarInfoId}")
    public R getByMapCarInfoId(@PathVariable("mapCarInfoId") Integer mapCarInfoId) {
        String byMapCarInfoId = deviceMapAreaCarInfoService.getByMapCarInfoId(mapCarInfoId);
        if(StringUtils.isNotBlank(byMapCarInfoId)){
            return R.ok(byMapCarInfoId);
        }else{
            return R.ok("该车辆未添加标签");
        }
    }
}
