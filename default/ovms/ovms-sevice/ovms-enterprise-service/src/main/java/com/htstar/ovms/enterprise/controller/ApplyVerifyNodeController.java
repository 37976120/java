package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.ApplyVerifyNode;
import com.htstar.ovms.enterprise.service.ApplyVerifyNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 审批节点
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
@RestController
@AllArgsConstructor
@RequestMapping("/applyverifynode" )
@Api(value = "applyverifynode", tags = "审批节点管理")
public class ApplyVerifyNodeController {

    private final  ApplyVerifyNodeService applyVerifyNodeService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param applyVerifyNode 审批节点
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getApplyVerifyNodePage(Page page, ApplyVerifyNode applyVerifyNode) {
        return R.ok(applyVerifyNodeService.page(page, Wrappers.query(applyVerifyNode)));
    }


    /**
     * 通过id查询审批节点
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(applyVerifyNodeService.getById(id));
    }

    /**
     * 新增审批节点
     * @param applyVerifyNode 审批节点
     * @return R
     */
    @ApiOperation(value = "新增审批节点", notes = "新增审批节点")
    @SysLog("新增审批节点" )
    @PostMapping
    public R save(@RequestBody ApplyVerifyNode applyVerifyNode) {
        return R.ok(applyVerifyNodeService.save(applyVerifyNode));
    }

    /**
     * 修改审批节点
     * @param applyVerifyNode 审批节点
     * @return R
     */
    @ApiOperation(value = "修改审批节点", notes = "修改审批节点")
    @SysLog("修改审批节点" )
    @PutMapping
    public R updateById(@RequestBody ApplyVerifyNode applyVerifyNode) {
        return R.ok(applyVerifyNodeService.updateById(applyVerifyNode));
    }

    /**
     * 通过id删除审批节点
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除审批节点", notes = "通过id删除审批节点")
    @SysLog("通过id删除审批节点" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(applyVerifyNodeService.removeById(id));
    }

}
