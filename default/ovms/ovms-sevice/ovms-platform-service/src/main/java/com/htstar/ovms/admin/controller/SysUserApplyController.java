package com.htstar.ovms.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.entity.SysUserApply;
import com.htstar.ovms.admin.api.req.ApplyJoinReq;
import com.htstar.ovms.admin.api.req.ApprovalJoinReq;
import com.htstar.ovms.admin.api.req.SysUserApplyPageReq;
import com.htstar.ovms.admin.api.vo.SysUserApplyVO;
import com.htstar.ovms.admin.service.SysUserApplyService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 企业员工申请加入
 *
 * @author flr
 * @date 2020-06-29 10:18:39
 */
@RestController
@AllArgsConstructor
@RequestMapping("/userapply" )
@Api(value = "userapply", tags = "企业员工申请加入管理")
public class SysUserApplyController {

    private final  SysUserApplyService sysUserApplyService;


    @Inner(value = false)
    @PostMapping("/applyJoin")
    @ApiOperation(value = "申请加入企业", notes = "申请加入企业")
    public R applyJoin(@RequestBody ApplyJoinReq req) {
        return sysUserApplyService.applyJoin(req);
    }



    @ApiOperation(value = "审批员工加入申请", notes = "审批员工加入申请")
    @SysLog("审批员工加入申请" )
    @PostMapping("/approval")
    public R approval(@RequestBody ApprovalJoinReq req) {
        return sysUserApplyService.approval(req);
    }

    /**
     * 分页查询
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/page" )
    public R<IPage<SysUserApplyVO>> getSysUserApplyPage(@RequestBody SysUserApplyPageReq req) {
        return sysUserApplyService.getSysUserApplyPage(req);
    }


    /**
     * 通过id查询企业员工申请加入
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(sysUserApplyService.getById(id));
    }

    /**
     * 新增企业员工申请加入
     * @param sysUserApply 企业员工申请加入
     * @return R
     */
    @ApiOperation(value = "新增企业员工申请加入", notes = "新增企业员工申请加入")
    @SysLog("新增企业员工申请加入" )
    @PostMapping
    public R save(@RequestBody SysUserApply sysUserApply) {
        return R.ok(sysUserApplyService.save(sysUserApply));
    }

    /**
     * 修改企业员工申请加入
     * @param sysUserApply 企业员工申请加入
     * @return R
     */
    @ApiOperation(value = "修改企业员工申请加入", notes = "修改企业员工申请加入")
    @SysLog("修改企业员工申请加入" )
    @PutMapping
    public R updateById(@RequestBody SysUserApply sysUserApply) {
        return R.ok(sysUserApplyService.updateById(sysUserApply));
    }

    /**
     * 通过id删除企业员工申请加入
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除企业员工申请加入", notes = "通过id删除企业员工申请加入")
    @SysLog("通过id删除企业员工申请加入" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(sysUserApplyService.removeById(id));
    }

}
