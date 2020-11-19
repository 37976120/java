package com.htstar.ovms.msg.api.feign;

import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.msg.api.entity.MsgPushLog;
import com.htstar.ovms.msg.api.req.BatchPushByUserIdsReq;
import com.htstar.ovms.msg.api.req.BroadAppPushByEtpReq;
import com.htstar.ovms.msg.api.req.SingleAppPushReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import java.util.List;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/8/12
 * Company: 航通星空
 * Modified By:
 */
@FeignClient(contextId = "msgAppPushFeign", value = ServiceNameConstants.MSG_SERVICE)
public interface MsgAppPushFeign {

    /**
     * Description: app 单点推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @PostMapping("/msgpush/appSinglePush")
    R appSinglePush(@Valid @RequestBody SingleAppPushReq req, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * Description: app 批次单点推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @PostMapping("/msgpush/batchAppSinglePush")
    R batchAppSinglePush(@Valid @RequestBody List<SingleAppPushReq> req, @RequestHeader(SecurityConstants.FROM) String from);


    @PostMapping("/msgpush/batchAppSinglePushUncode")
    R batchAppSinglePushUncode(@RequestBody  String req, @RequestHeader(SecurityConstants.FROM) String from);
    /**
     * Description: app 批量推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @PostMapping("/msgpush/batchPushByUserIds")
    R batchPushByUserIds(@Valid @RequestBody BatchPushByUserIdsReq req, @RequestHeader(SecurityConstants.FROM) String from);


    /**
     * Description: app 企业广播推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @PostMapping("/msgpush/broadAppPushByEtp")
    R broadAppPushByEtp(@Valid @RequestBody BroadAppPushByEtpReq req, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 新增消息推送记录持久化
     * @param msgPushLog 新增消息推送记录持久化
     * @return R
     */
    @PostMapping("/msgpush/msgpushlog")
     R saveMsgPushLogs(@RequestBody List<MsgPushLog> msgPushLog, @RequestHeader(SecurityConstants.FROM) String from);


    /**
     * Description: app 企业管理员广播推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    @PostMapping("/msgpush/broadAppPushByEtpRole")
    R EdpIdtoSingdemos(@RequestBody BroadAppPushByEtpReq msgPushStyle,@RequestHeader(SecurityConstants.FROM) String from);
}
