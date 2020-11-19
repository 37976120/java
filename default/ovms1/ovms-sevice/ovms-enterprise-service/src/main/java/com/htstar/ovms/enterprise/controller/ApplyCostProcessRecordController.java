package com.htstar.ovms.enterprise.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.ApplyCostProcessRecord;
import com.htstar.ovms.enterprise.api.req.ApplyCostProcessReq;
import com.htstar.ovms.enterprise.api.req.ApprovalRecordReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.req.WithdrawReq;
import com.htstar.ovms.enterprise.api.vo.ApplyCostProcessRecordVo;
import com.htstar.ovms.enterprise.service.ApplyCostProcessRecordService;

import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 费用审批记录
 *
 * @author lw
 * @date 2020-07-14 10:59:15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/costprocess" )
@Api(value = "costprocess", tags = "费用审批")
public class ApplyCostProcessRecordController {

    private final ApplyCostProcessRecordService applyCostProcessRecordService;


    /**
     * 分页查询
     * @param req
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/page" )
    public R<IPage<ApplyCostProcessRecordVo>> queryPage(@RequestBody ApprovalRecordReq req) {
        return applyCostProcessRecordService.queryPage(req);
    }

    @ApiOperation(value = "存档", notes = "存档")
    @GetMapping("/filing/{id}")
    public R filing(@PathVariable("id") Integer id) {
        return applyCostProcessRecordService.filing(id);
    }

    @ApiOperation(value = "退回", notes = "退回")
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody WithdrawReq req){
        return applyCostProcessRecordService.withdraw(req);
    }
    @ApiOperation(value = "导出", notes = "导出")
    @PostMapping("/export")
    public void exportExcel(@RequestBody ExportReq req) {
        applyCostProcessRecordService.exportExcel(req);
    }
}
