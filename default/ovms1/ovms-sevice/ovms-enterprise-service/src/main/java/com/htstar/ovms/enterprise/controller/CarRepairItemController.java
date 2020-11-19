package com.htstar.ovms.enterprise.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.CarRepairItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarRepairItemPageVO;
import com.htstar.ovms.enterprise.service.CarRepairItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 公车维修表

 *
 * @author lw
 * @date 2020-06-08 13:48:46
 */
@RestController
@AllArgsConstructor
@RequestMapping("/carrepairitem" )
@Api(value = "carrepairitem", tags = "公车维修")
@Slf4j
public class CarRepairItemController {

    private final  CarRepairItemService carRepairItemService;

    /**
     * 加油信息分页
     * @param
     * @param carItemPageReq
     * @return
     */
    @ApiOperation(value = "分页", notes = "分页")
    @PostMapping("/queryPage")
    public R<IPage<CarRepairItemPageVO>> queryPage(@RequestBody CarItemPageReq carItemPageReq) {
        return R.ok(carRepairItemService.queryPage(carItemPageReq));
    }
    /**
     * 通过id查询公车维修表

     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(carRepairItemService.getItemById(id));
    }

    /**
     * 新增公车维修表

     * @param carRepairItem 公车维修表

     * @return R
     */
    @ApiOperation(value = "新增 ", notes = "新增 ")
    @SysLog("新增 " )
    @PostMapping
    public R save(@RequestBody CarRepairItem carRepairItem) {
        return carRepairItemService.saveInfo(carRepairItem);
    }

    /**
     * 修改公车维修表
     * @param carRepairItem 公车维修表
     * @return R
     */
    @ApiOperation(value = "修改 ", notes = "修改 ")
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody CarRepairItem carRepairItem) {
        log.info("维修修改{}", JSONObject.toJSONString(carRepairItem));
        return  carRepairItemService.update(carRepairItem);
    }

    /**
     * 通过id删除公车维修表

     * @param ids
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除 ")
    @SysLog("通过id删除 " )
    @DeleteMapping("/{ids}" )
    public R removeById(@PathVariable String  ids) {
        return carRepairItemService.removeByIds(ids);
    }
    /**
     * 导出
     *
     * @param
     * @param response
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody ExportReq req)  {
        carRepairItemService.exportExcel(req);
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
        return carRepairItemService.filing(id,itemStatus);
    }

    @ApiOperation(value = "获取用户保存状态的信息", notes = "获取用户保存状态的信息")
    @GetMapping("/getItemByUser")
    public R<CarRepairItemPageVO> getItemByUser() {
        return R.ok(carRepairItemService.getItemByUser());
    }
}
