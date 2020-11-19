package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.CarEtcCard;

import com.htstar.ovms.enterprise.api.req.CarFileManageReq;
import com.htstar.ovms.enterprise.api.req.RechargeReq;
import com.htstar.ovms.enterprise.api.vo.EtcCardPageVO;
import com.htstar.ovms.enterprise.service.CarEtcCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;



/**
 * etc卡
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
@RestController
@AllArgsConstructor
@RequestMapping("/caretccard" )
@Api(value = "caretccard", tags = "etc卡管理")
public class CarEtcCardController {

    private final  CarEtcCardService carEtcCardService;


    /**
     * 分页
     * @param carFileManageReq
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/page" )
    public R<IPage<EtcCardPageVO>> queryPage(@RequestBody CarFileManageReq carFileManageReq) {
        return R.ok(carEtcCardService.queryPage(carFileManageReq));
    }


    /**
     * 通过id查询etc卡
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(carEtcCardService.getById(id));
    }

    /**
     * 新增etc卡
     * @param carEtcCard etc卡
     * @return R
     */
    @ApiOperation(value = "新增etc卡", notes = "新增etc卡")
    @SysLog("新增etc卡" )
    @PostMapping
    public R save(@RequestBody CarEtcCard carEtcCard) {
        return carEtcCardService.saveInfo(carEtcCard);
    }

    /**
     * 修改etc卡
     * @param carEtcCard etc卡
     * @return R
     */
    @ApiOperation(value = "修改etc卡", notes = "修改etc卡")
    @SysLog("修改etc卡" )
    @PutMapping
    public R updateById(@RequestBody CarEtcCard carEtcCard) {

        return carEtcCardService.updateEtcById(carEtcCard);
    }

    /**
     * 通过id删除etc卡
     * @param  ids
     * @return R
     */
    @ApiOperation(value = "通过id删除etc卡", notes = "通过id删除etc卡")
    @SysLog("通过id删除etc卡" )
    @DeleteMapping("/{ids}" )
    public R removeById(@PathVariable String  ids) {
        return carEtcCardService.removeByIds(ids);
    }


    /**
     * 导出
     * @param req
     */
    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody CarFileManageReq req){
        carEtcCardService.exportExcel(req);
    }

    @ApiOperation(value = "etc充值", notes = "etc充值")
    @SysLog("etc充值")
    @PostMapping("/recharge")
    public R recharge(@RequestBody RechargeReq recharge) {
       return carEtcCardService.recharge(recharge);
    }
}
