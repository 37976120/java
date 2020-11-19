package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.CarAccidItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarAccidItemPageVO;
import com.htstar.ovms.enterprise.service.CarAccidItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * 车辆事故信息
 *
 * @author lw
 * @date 2020-06-22 10:11:23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/caracciditem")
@Api(value = "caracciditem", tags = "车辆事故")
public class CarAccidItemController {

    private final CarAccidItemService carAccidItemService;

    /**
     * 分页查询
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/queryPage")
    public R<IPage<CarAccidItemPageVO>> getCarAccidItemPage(@RequestBody CarItemPageReq carItemPageReq) {
        return R.ok(carAccidItemService.queryPage(carItemPageReq));
    }


    /**
     * 通过id查询
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(carAccidItemService.getItemById(id));
    }

    /**
     * 新增
     *
     * @param carAccidItem
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增")
    @PostMapping
    //@PreAuthorize("@pms.hasPermission('admin_caracciditem_add')" )
    public R save(@RequestBody CarAccidItem carAccidItem) {
        return carAccidItemService.saveInfo(carAccidItem);
    }

    /**
     * 修改车辆事故信息
     *
     * @param carAccidItem 车辆事故信息
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改")
    @PutMapping
    //@PreAuthorize("@pms.hasPermission('admin_caracciditem_edit')" )
    public R updateById(@RequestBody CarAccidItem carAccidItem) {
        return R.ok(carAccidItemService.updateById(carAccidItem));
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return R
     */
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @SysLog("批量删除")
    @DeleteMapping("/{ids}")
    //@PreAuthorize("@pms.hasPermission('admin_caracciditem_del')" )
    public R removeById(@PathVariable String ids) {
        return carAccidItemService.removeByIds(ids);
    }

    @ApiOperation(value = "导出", notes = "导出")
    @SysLog("导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody ExportReq req){
        carAccidItemService.exportExcel(req);
    }

    /**
     * 存档
     * @param id
     * @param itemStatus
     * @return
     */
    @ApiOperation(value = "存档", notes = "存档")
    @GetMapping("filing/{id}/{itemStatus}" )
    public R filing(@PathVariable("id" ) Integer id,@PathVariable("itemStatus" ) Integer itemStatus) {
        return carAccidItemService.filing(id,itemStatus);
    }

    @ApiOperation(value = "获取用户保存状态的信息", notes = "获取用户保存状态的信息")
    @GetMapping("/getItemByUser")
    public R<CarAccidItemPageVO> getItemByUser() {
        return R.ok(carAccidItemService.getItemByUser());
    }
}