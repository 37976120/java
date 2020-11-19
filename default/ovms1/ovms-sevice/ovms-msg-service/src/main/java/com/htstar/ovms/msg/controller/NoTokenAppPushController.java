package com.htstar.ovms.msg.controller;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.common.security.annotation.Inner;

import com.htstar.ovms.msg.api.entity.MsgPushLog;
import com.htstar.ovms.msg.api.req.BatchPushByUserIdsReq;
import com.htstar.ovms.msg.api.req.BroadAppPushByEtpReq;
import com.htstar.ovms.msg.api.req.SingleAppPushReq;

import com.htstar.ovms.msg.service.MsgPushLogService;
import com.htstar.ovms.msg.service.MsgPushUserCidService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * @author JinZhu
 * @Description:免登陆，服务间调用
 * @date 2020/7/2811:08
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/msgpush" , produces = "application/json;charset=utf-8")
@ApiIgnore
public class NoTokenAppPushController {

    //消息发送Service层
    private final MsgPushUserCidService msgPushUserCidService;
    //消息持久层
    private final MsgPushLogService msgPushLogService;
    /**
     * Description:推送消息，向一个用户推送消息
     * @param
     * @return R
     */
    @PostMapping("/appSinglePush")
    @Inner
    R appSinglePush(@Valid @RequestBody SingleAppPushReq req){
        return R.ok(msgPushUserCidService.toSingdemo(req));
    }


    /**
     * Description: app 企业广播推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @PostMapping("/broadAppPushByEtp")
    @Inner
    R broadAppPushByEtp(@Valid @RequestBody BroadAppPushByEtpReq req){
        return R.ok(msgPushUserCidService.toSingdemos(req));
    }



    /**
     * Description: app 批次单点推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @PostMapping("/batchAppSinglePush")
    @Inner
    R batchAppSinglePush(@Valid @RequestBody List<SingleAppPushReq> req){
        return  R.ok(msgPushUserCidService.batchAppSinglePush(req));
    }


    @RequestMapping(value = "/batchAppSinglePushUncode",method = RequestMethod.POST)
    @Inner
    R batchAppSinglePushUncode(@RequestBody String req){
        return  R.ok(msgPushUserCidService.batchAppSinglePushReq(req));
    }

    /**
     * Description: app 批量推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @PostMapping("/batchPushByUserIds")
    @Inner
    R batchPushByUserIds(@Valid @RequestBody BatchPushByUserIdsReq req, @RequestHeader(SecurityConstants.FROM) String from){
        return   R.ok(msgPushUserCidService.batchPushByUserIds(req));
    }

    /**
     * 新增消息推送记录
     * @param msgPushLog 消息推送记录
     * @return R
     */
    @PostMapping("/msgpushlog")
    @Inner
    public R saveMsgPushLogs(@RequestBody List<MsgPushLog> msgPushLog, @RequestHeader(SecurityConstants.FROM) String from) {
        return R.ok(msgPushLogService.saveBatch(msgPushLog));
    }


    /**
     * Description: app 企业管理员广播推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @PostMapping("/broadAppPushByEtpRole")
    @Inner
    public R EdpIdtoSingdemos(@RequestBody BroadAppPushByEtpReq msgPushStyle,@RequestHeader(SecurityConstants.FROM) String from){
        return R.ok(msgPushUserCidService.EdpIdtoSingdemos(msgPushStyle));
    }

}
