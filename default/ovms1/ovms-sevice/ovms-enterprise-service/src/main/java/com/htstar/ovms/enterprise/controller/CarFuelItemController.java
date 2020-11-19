package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.CarFuelItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarFuelItemPageVO;
import com.htstar.ovms.enterprise.service.CarFuelItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * 公车加油表
 *
 * @author lw
 * @date 2020-06-08 13:48:44
 */
@RestController
@RequestMapping("/carfuelitem")
@Api(value = "carfuelitem", tags = "公车加油")
@AllArgsConstructor
@Slf4j
public class CarFuelItemController {
    private final CarFuelItemService carFuelItemService;

    /**
     * 加油信息分页
     * @param carItemPageReq
     * @return
     */
    @ApiOperation(value = "分页", notes = "分页")
    @PostMapping("/queryPage")
    public R<IPage<CarFuelItemPageVO>> queryPage(@RequestBody CarItemPageReq carItemPageReq) {
        return R.ok(carFuelItemService.queryPage(carItemPageReq));
    }



    /**
     * 通过id查询
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R<CarFuelItemPageVO> getById(@PathVariable("id") Integer id) {
        return R.ok(carFuelItemService.getCarFuelItemById(id));
    }

    /**
     * 新增
     *
     * @param carFuelItem
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增")
    @PostMapping
    public R save(@RequestBody CarFuelItem carFuelItem) {
        return carFuelItemService.saveInfo(carFuelItem);
    }

    /**
     * 修改
     *
     * @param carFuelItem 公车加油表
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改")
    @PutMapping
    public R updateById(@RequestBody CarFuelItem carFuelItem) {
//        log.info("请求数据为{}",JSONObject.toJSONString(carFuelItem) );
        return R.ok(carFuelItemService.updateFuelById(carFuelItem));
    }

    /**
     * 通过id删除
     *
     * @param ids
     * @return R
     */
    @ApiOperation(value = "删除", notes = "删除")
    @SysLog("通过id删除")
    @DeleteMapping("/{ids}")
    public R removeById(@PathVariable String ids) {
        return carFuelItemService.removeByIds(ids);
    }

    /**
     * 导出s
     *s
     *
     * @param response
     * @return
     */
    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody ExportReq req)  {
        carFuelItemService.exportExcel(req);
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
        return carFuelItemService.filing(id,itemStatus);
    }

    @ApiOperation(value = "获取用户保存状态的信息", notes = "获取用户保存状态的信息")
    @GetMapping("/getItemByUser")
    public R getItemByUser() {
        return R.ok(carFuelItemService.getItemByUser());
    }
}
