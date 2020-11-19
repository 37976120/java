package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.CarEtcItem;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarEtcItemPageVO;
import com.htstar.ovms.enterprise.service.CarEtcItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * etc通行记录
 *
 * @author lw
 * @date 2020-06-22 10:11:23
 */
@RestController
@AllArgsConstructor
@RequestMapping("/caretcitem")
@Api(value = "caretcitem", tags = "etc管理")
@Slf4j
public class CarEtcItemController {

    private final CarEtcItemService carEtcItemService;

    /**
     * 分页查询
     *
     * @param carItemPageReq
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/queryPage")
    public R<IPage<CarEtcItemPageVO>> getCarEtcItemPage(@RequestBody CarItemPageReq carItemPageReq) {
        return R.ok(carEtcItemService.queryPage(carItemPageReq));
    }


    /**
     * 通过id查询etc通行记录
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(carEtcItemService.getCarEtcItemById(id));
    }

    @ApiOperation(value = "获取用户保存状态的信息", notes = "获取用户保存状态的信息")
    @GetMapping("/getByUser")
    public R getByUser() {
        return R.ok(carEtcItemService.getCarEtcItemByUser());
    }
    /**
     * 新增etc通行记录
     *
     * @param carEtcItem etc通行记录
     * @return R
     */
    @ApiOperation(value = "新增 ", notes = "新增 ")
    @SysLog("新增")
    @PostMapping
    public R save(@RequestBody CarEtcItem carEtcItem) {
        return carEtcItemService.saveInfo(carEtcItem);
    }

    /**
     * 修改etc通行记录
     *
     * @param carEtcItem etc通行记录
     * @return R
     */
    @ApiOperation(value = "修改 ", notes = "修改")
    @SysLog("修改")
    @PutMapping
    public R updateById(@RequestBody CarEtcItem carEtcItem) {

        return carEtcItemService.updateEtcById(carEtcItem);
    }

    /**
     * 通过id删除etc通行记录
     *
     * @param ids
     * @return R
     */
    @ApiOperation(value = "批量删除", notes = "批量删除 ")
    @SysLog("批量删除 ")
    @DeleteMapping("/{ids}")
    public R removeById(@PathVariable String ids) {
        return R.ok(carEtcItemService.removeByIds(ids));
    }

    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody ExportReq req)  {
        carEtcItemService.exportExcel(req);
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
        return carEtcItemService.filing(id,itemStatus);
    }
}
