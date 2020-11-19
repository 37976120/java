package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.ApplyCostVerifyNode;
import com.htstar.ovms.enterprise.service.ApplyCostVerifyNodeService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 费用审批配置
 *
 * @author lw
 * @date 2020-07-14 10:59:15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/costVerifyNode" )
@Api(value = "costVerifyNode", tags = "费用审批配置")
public class ApplyCostVerifyNodeController {
    private final ApplyCostVerifyNodeService applyCostVerifyNodeService;

    /**
     * 新增费用审批配置
     * @param applyCostVerifyNode 费用审批配置
     * @return R
     */
    @ApiOperation(value = "更改费用审批配置", notes = "更改费用审批配置")
    @SysLog("更改费用审批配置" )
    @PostMapping
    public R save(@RequestBody ApplyCostVerifyNode applyCostVerifyNode) {
        return applyCostVerifyNodeService.saveCostVerifyNode(applyCostVerifyNode);
    }


    @ApiOperation(value = "获取最新配置", notes = "获取最新配置")
    @GetMapping("/getNow")
    public R<ApplyCostVerifyNode> getNow() {
        ApplyCostVerifyNode etpNowCostVerifyNode = applyCostVerifyNodeService.getEtpNowCostVerifyNode();
        return R.ok(etpNowCostVerifyNode);
    }

    @ApiOperation(value = "是否有审批权限 有为true 没有为false", notes = "是否有审批权限")
    @GetMapping("/isAuth")
    public R isAuth(){
        return R.ok(applyCostVerifyNodeService.isUpdateItem());
    }
}
