package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.CarOtherItem;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarOtherItemPageVO;
import com.htstar.ovms.enterprise.service.CarOtherItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 公车其他项目表

 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/carotheritem" )
@Api(value = "carotheritem", tags = "费用记录")
@Slf4j
public class CarOtherItemController {
    @Autowired
    private CarOtherItemService carOtherItemService;

    /**
     *
     * @param
     * @param carItemPageReq
     * @return
     */
    @ApiOperation(value = "分页", notes = "分页")
    @PostMapping("/queryPage")
    public R<IPage<CarOtherItemPageVO>> queryPage(@RequestBody CarItemPageReq carItemPageReq) {
        return R.ok(carOtherItemService.queryPage(carItemPageReq));
    }


    /**
     * 通过id查询公车其他项目表

     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(carOtherItemService.getItemById(id));
    }

    /**
     * 新增公车其他项目表
     * @param carOtherItem 公车其他项目表
     * @return R
     */
    @ApiOperation(value = "新增 ", notes = "新增 ")
    @SysLog("新增 " )
    @PostMapping
    public R save(@RequestBody CarOtherItem carOtherItem) {
        return carOtherItemService.saveInfo(carOtherItem);
    }

    /**
     * 修改公车其他项目表
     * @param carOtherItem 公车其他项目表
     * @return R
     */
    @ApiOperation(value = "修改 ", notes = "修改 ")
    @SysLog("修改 " )
    @PutMapping
    public R updateById(@RequestBody CarOtherItem carOtherItem) {
        return carOtherItemService.update(carOtherItem);
    }

    /**
     * 通过id删除公车其他项目表
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过id删除 ", notes = "通过id删除x")
    @SysLog("通过id删除 " )
    @DeleteMapping("/{ids}" )
    public R removeById(@PathVariable String ids) {
        return carOtherItemService.removeByIds(ids);
    }

    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody ExportReq req)  {
        carOtherItemService.exportExcel(req);
    }

    /**
     * 存档
     * @param id
     * @param itemStatus
     * @return
     */
    @ApiOperation(value = "存档", notes = "存档")
    @SysLog("存档")
    @GetMapping("filing/{id}/{itemStatus}"  )
    public R filing(@PathVariable("id" ) Integer id,@PathVariable("itemStatus" ) Integer itemStatus) {
        return carOtherItemService.filing(id,itemStatus);
    }

    @ApiOperation(value = "批量保存", notes = "批量保存")
    @SysLog("批量保存")
    @PostMapping("/batchSave")
    public R batchSave(@RequestBody  CarOtherItem carOtherItem){
        return carOtherItemService.batchSave(carOtherItem);
    }

    @ApiOperation(value = "获取用户保存状态的信息", notes = "获取用户保存状态的信息")
    @GetMapping("/getItemByUser/{itemType}")
    public R<CarOtherItemPageVO> getItemByUser(@PathVariable("itemType") Integer itemType) {
        return R.ok(carOtherItemService.getItemByUser(itemType));
    }


    @ApiOperation(value = "批量保存暂存", notes = "批量保存暂存")
    @PostMapping("/temporarySave")
    public R temporarySave (@RequestBody CarOtherItemPageVO carOtherItemPageVO) {
        return carOtherItemService.temporarySave(carOtherItemPageVO);
    }

    @ApiOperation(value = "获取暂存数据", notes = "获取暂存数据")
    @GetMapping("/getTemporarySave")
    public R getTemporarySave () {
        return carOtherItemService.getTemporarySave();
    }
}
