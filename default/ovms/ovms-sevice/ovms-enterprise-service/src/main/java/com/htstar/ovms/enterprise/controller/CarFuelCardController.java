package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.CarFuelCard;
import com.htstar.ovms.enterprise.api.req.CarFileManageReq;
import com.htstar.ovms.enterprise.api.req.RechargeReq;
import com.htstar.ovms.enterprise.api.vo.FuelCardPageVO;
import com.htstar.ovms.enterprise.service.CarFuelCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * 油卡
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
@RestController
@AllArgsConstructor
@RequestMapping("/carfuelcard" )
@Api(value = "carfuelcard", tags = "油卡管理")
@Slf4j
public class CarFuelCardController {

    private final  CarFuelCardService carFuelCardService;


    /**
     * 分页查询
     * @param carFileManageReq
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/queryPage" )
    public R<IPage<FuelCardPageVO>> queryPage(@RequestBody CarFileManageReq carFileManageReq) {
        return R.ok(carFuelCardService.queryPage(carFileManageReq));
    }


    /**
     * 通过id查询油卡
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(carFuelCardService.getById(id));
    }

    /**
     * 新增油卡
     * @param carFuelCard 油卡
     * @return R
     */
    @ApiOperation(value = "新增油卡", notes = "新增油卡")
    @SysLog("新增油卡" )
    @PostMapping
    //@PreAuthorize("@pms.hasPermission('enterprise_carfuelcard_add')" )
    public R save(@RequestBody CarFuelCard carFuelCard) {

        return carFuelCardService.saveInfo(carFuelCard);
    }

    /**
     * 修改油卡
     * @param carFuelCard 油卡
     * @return R
     */
    @ApiOperation(value = "修改油卡", notes = "修改油卡")
    @SysLog("修改油卡" )
    @PutMapping
    public R updateById(@RequestBody CarFuelCard carFuelCard) {
        return carFuelCardService.updateFuelById(carFuelCard);
    }

    /**
     * 批量删除
     * @param ids
     * @return R
     */
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @SysLog("批量删除" )
    @DeleteMapping("/{ids}" )
    public R removeById(@PathVariable String ids) {
        return carFuelCardService.removeByIds(ids);
    }

    /**
     * 导出
     * @param carFileManageExportReq
     * @param response
     */
    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody CarFileManageReq req){
        carFuelCardService.exportExcel(req);
    }

    @ApiOperation(value = "油卡充值", notes = "油卡充值")
    @SysLog("油卡充值")
    @PostMapping("/recharge")
    public R recharge(@RequestBody RechargeReq recharge) {
        return carFuelCardService.recharge(recharge);
    }
}
