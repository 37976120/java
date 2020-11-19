package com.htstar.ovms.enterprise.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.CarInsItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarInsItemPageVO;
import com.htstar.ovms.enterprise.service.CarInsItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 公车保险表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/carinsitem" )
@Api(value = "carinsitem", tags = "公车保险")
@Slf4j
public class CarInsItemController {

    private final  CarInsItemService carInsItemService;

    /**
     * 保险信息分页
     * @param
     * @param carItemPageReq
     * @return
     */
    @ApiOperation(value = "保险分页", notes = "保险分页")
    @PostMapping("/queryPage")
    public R<IPage<CarInsItemPageVO>> queryPage(@RequestBody  CarItemPageReq carItemPageReq) {
        log.info("保险分页查询条件{}", JSONObject.toJSONString(carItemPageReq));
        return R.ok(carInsItemService.queryPage(carItemPageReq));
    }

    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R<CarInsItemPageVO> getById(@PathVariable("id" ) Integer id) {
        return R.ok(carInsItemService.getItemById(id));
    }

    /**
     * 新增公车保险
     * @param carInsItem 公车保险表
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody CarInsItem carInsItem) {
        return carInsItemService.saveInfo(carInsItem);
    }

    /**
     * 修改公车保险
     * @param carInsItem
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody CarInsItem carInsItem) {

        return carInsItemService.update(carInsItem);
    }

    /**
     * 通过id删除
     * @param
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除" )
    @DeleteMapping("/{ids}" )
    public R removeById(@PathVariable String ids) {
        return  carInsItemService.removeByIds(ids);
    }

    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody ExportReq req) {
        carInsItemService.exportExcel(req);
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
        return carInsItemService.filing(id,itemStatus);
    }


    @ApiOperation(value = "获取用户保存状态的信息", notes = "获取用户保存状态的信息")
    @GetMapping("/getItemByUser")
    public R<CarInsItemPageVO> getItemByUser() {
        return R.ok(carInsItemService.getItemByUser());
    }
}
