package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.CarMaiItem;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarMaiItemPageVO;
import com.htstar.ovms.enterprise.service.CarMaiItemService;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * 公车保养表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/carmaiitem" )
@Api(value = "carmaiitem", tags = "保养信息")
@Slf4j
public class CarMaiItemController {

    private final  CarMaiItemService carMaiItemService;

    @ApiOperation(value = "保养信息分页", notes = "保养信息分页")
    @PostMapping("/queryPage")
    public R<IPage<CarMaiItemPageVO>> queryPage(@RequestBody  CarItemPageReq carItemPageReq) {
        return R.ok(carMaiItemService.queryPage(carItemPageReq));
    }

    /**
     * 通过id查询公车保养表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(carMaiItemService.getItemById(id));
    }

    /**
     * 新增公车保养表
     * @param carMaiItem 公车保养表
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody CarMaiItem carMaiItem) {
        return carMaiItemService.saveInfo(carMaiItem);
    }

    /**
     * 修改公车保养表
     * @param carMaiItem 公车保养表
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody CarMaiItem carMaiItem) {
        return carMaiItemService.update(carMaiItem);
    }

    /**
     * 通过id删除公车保养表
     * @param ids
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除" )
    @DeleteMapping("/{ids}" )
    public R removeById(@PathVariable String  ids) {
        log.info("删除数据为{}",ids );
        return R.ok(carMaiItemService.removeByIds(ids));
    }

    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody ExportReq req) {
        carMaiItemService.exportExcel(req);
    }

    /**
     * 存档
     * @param id
     * @param itemStatus
     * @return
     */
    @ApiOperation(value = "存档", notes = "存档")
    @SysLog("存档")
    @GetMapping("filing/{id}/{itemStatus}" )
    public R filing(@PathVariable("id" ) Integer id,@PathVariable("itemStatus" ) Integer itemStatus) {
        return carMaiItemService.filing(id,itemStatus);
    }


    @ApiOperation(value = "获取用户保存状态的信息", notes = "获取用户保存状态的信息")
    @GetMapping("/getItemByUser")
    public R<CarMaiItemPageVO> getItemByUser() {
        return R.ok(carMaiItemService.getItemByUser());
    }
}
