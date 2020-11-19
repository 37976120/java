package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.req.*;
import com.htstar.ovms.enterprise.api.vo.ApplyCarOrderVO;
import com.htstar.ovms.enterprise.api.vo.ApplyOrderPageVO;
import com.htstar.ovms.enterprise.service.ApplyCarOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Description: 用车申请
 * Author: flr
 * Date: 2020-06-30 18:24:24
 * Company: 航通星空
 * Modified By:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/applycare" )
@Api(value = "用车申请", tags = "用车申请")
public class ApplyCarOrderController {

    private final ApplyCarOrderService carOrderService;

    /**
     * 提交公车申请
     */
    @ApiOperation(value = "提交公车申请", notes = "提交公车申请")
    @SysLog("提交公车申请" )
    @PostMapping("/commitOfficeCarApply")
    public R commitOfficeCarApply(@RequestBody ApplyCarOrderReq req) {
        return carOrderService.commitOfficeCarApply(req);
    }

    /**
     * 处理公车申请
     */
    @ApiOperation(value = "处理公车申请", notes = "处理公车申请")
    @SysLog("处理公车申请" )
    @PostMapping("/handleOfficeCarApply")
    public R handleOfficeCarApply(@RequestBody HandleOfficeCarApplyReq req) {
        return carOrderService.handleOfficeCarApply(req);
    }

    /**
     * 普通分页查询
     */
    @ApiOperation(value = "分页", notes = "分页")
    @PostMapping("/page" )
    public R<Page<ApplyOrderPageVO>> page(@RequestBody PageApplyOrderReq req) {
        return carOrderService.getPage(req);
    }

    /**
     * 详情
     */
    @ApiOperation(value = "详情", notes = "详情")
    @GetMapping("/{id}" )
    public R<ApplyCarOrderVO> getVOById(@PathVariable("id" ) Integer id) {
        return carOrderService.getVOById(id);
    }



    /**
     * 分配车辆
     */
    @ApiOperation(value = "分配车辆", notes = "分配车辆")
    @PostMapping("/distributionCar" )
    public R distributionCar(@RequestBody DistributionCarReq req) {
        return carOrderService.distributionCar(req);
    }

    /**
     * 分配司机
     */
    @ApiOperation(value = "分配司机", notes = "分配司机")
    @PostMapping("/distributionDriver" )
    public R distributionDriver(@RequestBody DistributionDriverReq req) {
        return carOrderService.distributionDriver(req);
    }


    /**
     * 提车上报
     */
    @ApiOperation(value = "提车上报", notes = "提车上报")
    @PostMapping("/saveGiveCarData" )
    public R saveGiveCarData(@RequestBody SaveGiveCarDataReq req) {
        return carOrderService.saveGiveCarData(req);
    }


    /**
     * 还车上报
     */
    @ApiOperation(value = "还车上报", notes = "还车上报")
    @PostMapping("/saveReturnCarData" )
    public R saveReturnCarData(@RequestBody SaveReturnCarDataReq req) {
        return carOrderService.saveReturnCarData(req);
    }

}
