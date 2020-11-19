package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.CarDriverInfo;
import com.htstar.ovms.enterprise.api.req.CarFileManageReq;
import com.htstar.ovms.enterprise.api.vo.CarDriverPageVO;
import com.htstar.ovms.enterprise.service.CarDriverInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


/**
 * 司机
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cardriverinfo" )
@Api(value = "cardriverinfo", tags = "司机管理")
@Slf4j
public class CarDriverInfoController {

    private final  CarDriverInfoService carDriverInfoService;

    @ApiOperation(value = "分页", notes = "分页")
    @PostMapping("/queryPage")
    public R<IPage<CarDriverPageVO>> queryPage(@RequestBody CarFileManageReq carFileManageReq) {
        return R.ok(carDriverInfoService.queryPage(carFileManageReq));
    }

    /**
     * 通过id查询司机
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(carDriverInfoService.getById(id));
    }

    /**
     * 新增司机
     * @param carDriverInfo 司机
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R saveDriver(@RequestBody CarDriverInfo carDriverInfo) {
        return carDriverInfoService.saveInfo(carDriverInfo);
    }
    @ApiOperation(value = "注册时添加司机", notes = "注册时添加司机")
    @GetMapping("/saveDriver/{userId}/{etpId}")
    public R saveDriverByUserId(@PathVariable("userId") Integer userId,@PathVariable("etpId")Integer etpId){
        return R.ok(carDriverInfoService.saveDriverByUserId(userId,etpId ));
    }
    @ApiOperation(value = "删除用户时删除司机", notes = "删除用户时删除司机")
    @GetMapping("/delDriver/{userId}")
    public R delDriverByUserId(@PathVariable("userId") Integer userId){
        return R.ok(carDriverInfoService.delDriverByUserId(userId));
    }
    /**
     * 修改司机
     * @param carDriverInfo 司机
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @SysLog("修改司机" )
    @PutMapping
    public R updateById(@RequestBody CarDriverInfo carDriverInfo) {
        return carDriverInfoService.updateDriverById(carDriverInfo);
    }

    /**
     * 通过id删除司机
     * @param ids
     * @return R
     */
    @ApiOperation(value = "批量删除", notes = "批量删除")
    @SysLog("批量删除" )
    @DeleteMapping("/{ids}" )
    public R removeByIds(@PathVariable String ids) {
        return carDriverInfoService.removeByIds(ids);
    }

    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody CarFileManageReq req){
        carDriverInfoService.exportExcel(req);
    }
}
