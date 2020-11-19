package com.htstar.ovms.device.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.device.api.dto.FenceDTO;
import com.htstar.ovms.device.api.entity.CarFenceRelation;
import com.htstar.ovms.device.api.entity.Fence;
import com.htstar.ovms.device.api.vo.CarFenceRelationVO;
import com.htstar.ovms.device.api.vo.FencePageVO;
import com.htstar.ovms.device.service.CarFenceRelationService;
import com.htstar.ovms.device.service.FenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 围栏
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/fence" )
@Api(value = "fence", tags = "围栏管理")
public class FenceController {

    private final  FenceService fenceService;

    private final CarFenceRelationService carFenceRelationService;
    /**
     * 按照围栏分页查询
     * @param fenceDTO 查询条件
     * @return
     */
    @ApiOperation(value = "按照围栏分页查询", notes = "按照围栏分页查询")
    @PostMapping("/page" )
    public R<Page<FencePageVO>> getFencePage(@RequestBody FenceDTO fenceDTO) {
        return R.ok(fenceService.getFencePage(fenceDTO));
    }

    /**
     * 根据围栏id查询绑定车辆列表
     */
    @ApiOperation(value = "根据围栏id查询绑定车辆列表", notes = "根据围栏id查询绑定车辆列表")
    @PostMapping("/getCarFenceRelationPage" )
    public R<Page<CarFenceRelationVO>> getCarFenceRelationPage(@RequestBody FenceDTO fenceDTO) {
        return R.ok(carFenceRelationService.getCarFenceRelationPage(fenceDTO));
    }

    /**
     * 查询没有绑定车辆信息列表
     */
    @ApiOperation(value = "查询没有绑定车辆信息列表", notes = "查询没有绑定车辆信息列表")
    @PostMapping("/getAddCarFenCenInfoPage" )
    public R<Page<CarFenceRelationVO>> getAddCarFenCenInfoPage(@RequestBody FenceDTO fenceDTO) {
        return R.ok(carFenceRelationService.getAddCarFenCenInfoPage(fenceDTO));
    }

    /**
     * 添加围栏和车辆关联
     */
    @ApiOperation(value = "添加围栏和车辆关联", notes = "添加围栏和车辆关联")
    @SysLog("添加围栏和车辆关联" )
    @PostMapping("/addFenceAndCarRelation" )
    public R addFenceAndCarRelation(@RequestBody List<CarFenceRelation> carFenceRelationList) {
        return R.ok(carFenceRelationService.saveBatch(carFenceRelationList));
    }

    /**
     * 取消围栏和车辆的关联关系
     * @param id 围栏和车辆绑定表id
     * @return
     */
    @ApiOperation(value = "取消围栏和车辆的关联关系", notes = "取消围栏和车辆的关联关系")
    @SysLog("取消围栏和车辆的关联关系" )
    @DeleteMapping("/deleteRelevanceRelation/{id}" )
    public R deleteRelevanceRelation(@PathVariable("id") Integer id) {
        return R.ok(carFenceRelationService.removeById(id));
    }

    /**
     * 按车辆查询绑定围栏数
     */
    @ApiOperation(value = "按车辆查询绑定围栏数", notes = "按车辆查询绑定围栏数")
    @PostMapping("/fenceRelationCarInfoPage" )
    public R<Page<CarFenceRelationVO>> fenceRelationCarInfoPage(@RequestBody FenceDTO fenceDTO) {
        return R.ok(carFenceRelationService.fenceRelationCarInfoPage(fenceDTO));
    }

    /**
     * 查询未被车辆绑定的围栏信息
     */
    @ApiOperation(value = "查询未被车辆绑定的围栏信息", notes = "查询未被车辆绑定的围栏信息")
    @PostMapping("/addCarInfoFencePage" )
    public R<Page<FencePageVO>> getNotAddFenceByCarInfoPage(@RequestBody FenceDTO fenceDTO) {
        return R.ok(fenceService.getNotAddFenceByCarInfoPage(fenceDTO));
    }

    /**
     * 根据车辆id查询绑定围栏信息
     */
    @ApiOperation(value = "根据车辆id查询绑定围栏信息", notes = "根据车辆id查询绑定围栏信息")
    @PostMapping("/selectFenceByCarIdPage" )
    public R<Page<CarFenceRelationVO>> selectFenceByCarIdPage(@RequestBody FenceDTO fenceDTO) {
        return R.ok(carFenceRelationService.selectFenceByCarIdPage(fenceDTO));
    }

    /**
     * 通过id查询围栏
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(fenceService.getById(id));
    }

    /**
     * 新增围栏
     * @param fence 围栏
     * @return R
     */
    @ApiOperation(value = "新增围栏", notes = "新增围栏")
    @SysLog("新增围栏" )
    @PostMapping
    public R save(@RequestBody Fence fence) {
        return fenceService.saveFence(fence);
    }

    /**
     * 修改围栏
     * @param fence 围栏
     * @return R
     */
    @ApiOperation(value = "修改围栏", notes = "修改围栏")
    @SysLog("修改围栏" )
    @PutMapping
    public R updateById(@RequestBody Fence fence) {
        return fenceService.updateFence(fence);
    }

    /**
     * 通过id删除围栏
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除围栏", notes = "通过id删除围栏")
    @SysLog("通过id删除围栏" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        //true 存在 false 不存在
        boolean exit = carFenceRelationService.getExits(id);
        return fenceService.removeFence(id,exit);
    }

}
