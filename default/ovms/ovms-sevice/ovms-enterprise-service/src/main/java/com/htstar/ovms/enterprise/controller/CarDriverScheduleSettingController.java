package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.dto.CarDriverScheduleDTO;
import com.htstar.ovms.enterprise.api.dto.CarDriverScheduleNoPageDTO;
import com.htstar.ovms.enterprise.api.entity.CarDriverScheduleSetting;
import com.htstar.ovms.enterprise.api.vo.ApplyCarOrderAndDriverVO;
import com.htstar.ovms.enterprise.api.vo.CarDriverScheduleSettingVO;
import com.htstar.ovms.enterprise.service.CarDriverScheduleSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 设置车辆或者司机排班规则
 *
 * @author HanGuJi
 * @date 2020-07-01 15:03:18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cardriverschedulesetting")
@Api(value = "cardriverschedulesetting", tags = "设置车辆或者司机排班规则管理")
public class CarDriverScheduleSettingController {

    private final CarDriverScheduleSettingService carDriverScheduleSettingService;

    /**
     * 分页查询
     *
     * @param page                     分页对象
     * @param carDriverScheduleSetting 设置车辆或者司机排班规则
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getCarDriverScheduleSettingPage(Page page, CarDriverScheduleSetting carDriverScheduleSetting) {
        return R.ok(carDriverScheduleSettingService.page(page, Wrappers.query(carDriverScheduleSetting)));
    }


    /**
     * 通过车辆id或者司机id查询车辆或者司机排班规则
     * @param carAndDriverId carAndDriverId
     * @param carAndDriverId flag
     * @return R
     */
    @ApiOperation(value = "通过车辆id或者司机id查询车辆或者司机排班规则", notes = "通过车辆id或者司机id查询车辆或者司机排班规则")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carAndDriverId",value = "车辆id或者司机id",required = true,dataType = "Integer"),
            @ApiImplicitParam(name = "flag",value = "1 查询车辆信息 其他 司机信息",required = true, dataType = "Integer")
    })
    @GetMapping("/{carAndDriverId}/{flag}")
    public R<CarDriverScheduleSettingVO> getById(@PathVariable("carAndDriverId") Integer carAndDriverId, @PathVariable("flag") Integer flag) {

        return R.ok(carDriverScheduleSettingService.getCarOrDriverScheduleSetting(carAndDriverId,flag));
    }

    /**
     * 新增\修改设置车辆或者司机排班规则
     *
     * @param carDriverScheduleSetting 设置车辆或者司机排班规则
     * @return R
     */
    @ApiOperation(value = "新增\\修改设置车辆或者司机排班规则", notes = "新增\\修改设置车辆或者司机排班规则")
    @SysLog("新增\\修改设置车辆或者司机排班规则")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('admin_cardriverschedulesetting_add')" )
    public R save(@RequestBody CarDriverScheduleSetting carDriverScheduleSetting) {
        return R.ok(carDriverScheduleSettingService.saveScheduleSetting(carDriverScheduleSetting));
    }


    /**
     * 通过id删除设置车辆或者司机排班规则
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除设置车辆或者司机排班规则", notes = "通过id删除设置车辆或者司机排班规则")
    @SysLog("通过id删除设置车辆或者司机排班规则")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('admin_cardriverschedulesetting_del')")
    public R removeById(@PathVariable Integer id) {
        return R.ok(carDriverScheduleSettingService.removeById(id));
    }


    /**
     * 可以分配的车辆和司机列表
     * @param carDriverScheduleDTO
     * @return
     */
    @ApiOperation(value = "可以分配的车辆和司机列表", notes = "可以分配的车辆和司机列表")
    @PostMapping("/getAbleAllocationCarAndDriver")
    public R<Page<ApplyCarOrderAndDriverVO>> getAbleAllocationCarAndDriver(@RequestBody CarDriverScheduleDTO carDriverScheduleDTO) {
        return R.ok(carDriverScheduleSettingService.getAbleAllocationCarAndDriver(carDriverScheduleDTO));
    }


    /**
     * 车辆和司机排班表
     * @param carDriverScheduleDTO 车辆和司机排班表
     * @return
     */
    @ApiOperation(value = "车辆和司机排班表", notes = "车辆和司机排班表")
    @PostMapping("/getCarDriverSchedulePage")
    public R<Page<ApplyCarOrderAndDriverVO>> getCarDriverSchedulePage(@RequestBody CarDriverScheduleDTO carDriverScheduleDTO) {
        return R.ok(carDriverScheduleSettingService.getCarDriverSchedulePage(carDriverScheduleDTO));
    }
    /**
     * 车辆和司机排班表
     * @param carDriverScheduleDTO 车辆和司机排班表
     * @return
     */
    @ApiOperation(value = "车辆和司机排班表总数", notes = "车辆和司机排班表总数")
    @PostMapping("/carInfoDevicetotal")
    public R<Integer> carInfoDevicetotal(@RequestBody CarDriverScheduleNoPageDTO carDriverScheduleDTO) {
        return R.ok(carDriverScheduleSettingService.carInfoDevicetotal(carDriverScheduleDTO));
    }

}
