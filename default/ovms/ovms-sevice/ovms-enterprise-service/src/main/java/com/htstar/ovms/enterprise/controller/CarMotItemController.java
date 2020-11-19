package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.CarMotItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarMotItemPageVO;
import com.htstar.ovms.enterprise.service.CarMotItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * 公车年检表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/carmotitem" )
@Api(value = "carmotitem", tags = "公车年检")
@Slf4j
public class CarMotItemController {

    private final  CarMotItemService carMotItemService;

    /**
     * 加油信息分页
     * @param
     * @param carItemPageReq
     * @return
     */
    @ApiOperation(value = "分页", notes = "分页")
    @PostMapping("/queryPage")
    public R<IPage<CarMotItemPageVO>> queryPage(@RequestBody CarItemPageReq carItemPageReq) {
        return R.ok(carMotItemService.queryPage(carItemPageReq));
    }

    /**
     * 通过id查询公车年检表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(carMotItemService.getItemById(id));
    }

    /**
     * 新增
     * @param carMotItem 公车年检
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody CarMotItem carMotItem) {
        return carMotItemService.saveInfo(carMotItem);
    }

    /**
     * 修改
     * @param carMotItem 公车年检表
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改" )
    @PutMapping
    public R updateById(@RequestBody CarMotItem carMotItem) {
        return carMotItemService.update(carMotItem);
    }

    /**
     * 通过id删除
     * @param ids
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除" )
    @DeleteMapping("/{ids}" )
    public R removeByIds(@PathVariable String  ids) {
        return carMotItemService.removeByIds(ids);
    }
    /**
     * 导出
     *
     * @param
     * @param response
     * @return
     * @throws
     */
    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody  ExportReq req) {
        carMotItemService.exportExcel(req);
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
        return carMotItemService.filing(id,itemStatus);
    }


    @ApiOperation(value = "获取用户保存状态的信息", notes = "获取用户保存状态的信息")
    @GetMapping("/getItemByUser")
    public R<CarMotItemPageVO> getItemByUser() {
        return R.ok(carMotItemService.getItemByUser());
    }
}
