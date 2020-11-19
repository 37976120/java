package com.htstar.ovms.enterprise.controller;

import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.req.CarProcessReq;
import com.htstar.ovms.enterprise.api.req.SetNodeVerifyUserReq;
import com.htstar.ovms.enterprise.api.vo.ApplyCarProcessVO;
import com.htstar.ovms.enterprise.api.vo.NodeVerifyUserVO;
import com.htstar.ovms.enterprise.service.ApplyCarProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 公车申请流程
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
@RestController
@AllArgsConstructor
@RequestMapping("/process" )
@Api(value = "用车申请流程", tags = "用车申请流程")
public class ApplyCarProcessController {

    private final ApplyCarProcessService processService;


    @ApiOperation(value = "用车申请流程配置", notes = "用车申请流程配置")
    @SysLog("用车申请流程配置" )
    @PostMapping("/configOfficeCarProcess")
    public R configOfficeCarProcess(@RequestBody CarProcessReq req) {
        return processService.configOfficeCarProcess(req);
    }

    @ApiOperation(value = "根据ID获取流程", notes = "用车申请流程配置")
    @GetMapping("/getById/{id}")
    public R<ApplyCarProcessVO> getById(@PathVariable("id" )Integer id) {
        return processService.getVoById(id);
    }

    @ApiOperation(value = "企业获取当前流程", notes = "企业获取当前流程")
    @GetMapping("/getEtpNowProcess/{processType}")
    public R<ApplyCarProcessVO> getEtpNowProcess(@PathVariable("processType" )@ModelAttribute("流程类型:0=公车申请；1=私车公用；")Integer processType) {
        return processService.getEtpNowProcess(processType);
    }


    @ApiOperation(value = "获取节点的审批人员", notes = "获取节点的审批人员")
    @GetMapping("/getNodeVerifyUser/{nodeId}")
    public R<NodeVerifyUserVO> getNodeVerifyUser(@PathVariable("nodeId" )@ModelAttribute("节点ID")Integer nodeId) {
        return processService.getNodeVerifyUser(nodeId);
    }


    @ApiOperation(value = "设置节点的审批人员", notes = "设置节点的审批人员")
    @PostMapping("/setNodeVerifyUser")
    public R setNodeVerifyUser(@RequestBody SetNodeVerifyUserReq req) {
        return processService.setNodeVerifyUser(req);
    }

}
