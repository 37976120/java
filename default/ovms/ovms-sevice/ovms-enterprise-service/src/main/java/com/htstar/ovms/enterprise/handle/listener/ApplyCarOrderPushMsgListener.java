package com.htstar.ovms.enterprise.handle.listener;

import com.htstar.ovms.admin.api.feign.RemoteUserService;
import com.htstar.ovms.admin.api.vo.UserMsgVO;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.enterprise.api.constant.NodeVerifyTypeConstant;
import com.htstar.ovms.enterprise.api.constant.ProcessStatusConstant;
import com.htstar.ovms.enterprise.api.entity.ApplyCarOrder;
import com.htstar.ovms.enterprise.api.entity.ApplyOrderTask;
import com.htstar.ovms.enterprise.handle.event.ApplyCarOrderPushMsgEvent;
import com.htstar.ovms.enterprise.msg.template.UseCarTemlateEnum;
import com.htstar.ovms.enterprise.service.ApplyVerifyNodeService;
import com.htstar.ovms.msg.api.constant.MsgTypeConstant;
import com.htstar.ovms.msg.api.feign.MsgAppPushFeign;
import com.htstar.ovms.msg.api.req.SingleAppPushReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 用车申请消息事件监听
 * Author: flr
 * Date: Created in 2020/8/13
 * Company: 航通星空
 * Modified By:
 */
@Component
@Slf4j
public class ApplyCarOrderPushMsgListener {
    @Autowired
    private ApplyVerifyNodeService nodeService;
    @Autowired
    private MsgAppPushFeign msgAppPushFeign;
    @Autowired
    private RemoteUserService userService;

    @EventListener
    @Async
    public void onApplicationEvent(ApplyCarOrderPushMsgEvent event) {
        ApplyCarOrder order = event.getOrder();
        UseCarTemlateEnum template =  this.getTemplateStr(order.getProcessStatus().intValue());
        if (null == template){
//            log.warn("当前用车任务找不到推送模板",event);
            return;
        }

        List<ApplyOrderTask> taskList = event.getOrderTaskList();
//        ApplyVerifyNode node = nodeService.getNode(order.getProcessId(),order.getNextNodeType());
//        if (node == null || node.getSmsNotifyStatus().intValue() == 0) {
//            //不需要提醒
//            return;
//        }


        String applyNickName = nodeService.getNickName(order.getApplyUserId());
        List<SingleAppPushReq> reqList = new ArrayList<>();
        List<UserMsgVO> userRoleMsgVOList = new ArrayList<>();
        List<UserMsgVO> userMsgVOList = new ArrayList<>();

        List<Integer> userIdList = new ArrayList<>();
        for (ApplyOrderTask task: taskList){
            if (task.getVerifyType().intValue() == NodeVerifyTypeConstant.ROLE_TASK){
                //角色类型的消息推送
                    userRoleMsgVOList = userService.getUserMsgVOListByRoleId(task.getVerifyRoleId(), SecurityConstants.FROM_IN);
            }else {
                //用户类型推送
                userIdList.add(task.getVerifyUserId());
            }
        }

        if (!userIdList.isEmpty()){
            userMsgVOList = userService.getUserMsgVOListByIds(userIdList, SecurityConstants.FROM_IN);
        }

        userMsgVOList.addAll(userRoleMsgVOList);

        if (null == userMsgVOList || userMsgVOList.isEmpty()){
            log.warn("当前用车任务找不到推送人",event);
            return;
        }

        for (UserMsgVO userMsgVO : userMsgVOList){
            SingleAppPushReq req = new SingleAppPushReq();
            req.setMsgType(MsgTypeConstant.VEHICEL_NOTIFICATION);
            req.setAppCarId(order.getOrderId());
            req.setUserId(userMsgVO.getUserId());
            req.setTitle(template.getTitle());
            req.setContent(MessageFormat.format(template.getContent(), userMsgVO.getNickName()));
            if (order.getProcessStatus().intValue() == ProcessStatusConstant.WAIT_VERIFY
                || order.getProcessStatus().intValue() == ProcessStatusConstant.WAIT_RECIVE
                || order.getProcessStatus().intValue() == ProcessStatusConstant.WAIT_DISTRIBUTION){
                req.setContent(MessageFormat.format(template.getContent(), applyNickName));
            }else {
                req.setContent(MessageFormat.format(template.getContent(), userMsgVO.getNickName()));
            }

            reqList.add(req);
        }
        if (reqList.isEmpty()){
            return;
        }
        try{
            msgAppPushFeign.batchAppSinglePush(reqList,SecurityConstants.FROM_IN);
        }catch (Exception e){
            log.error("调取消息服务推送消息失败",e);
        }
    }

    private UseCarTemlateEnum getTemplateStr(int processStatus) {
        switch (processStatus){
            case ProcessStatusConstant.WAIT_VERIFY:
                return UseCarTemlateEnum.WAITE_VERIFY;
            case ProcessStatusConstant.WAIT_DISTRIBUTION:
                return UseCarTemlateEnum.WAITE_GIVE_CAR;
            case ProcessStatusConstant.WAIT_GIVECAR:
                return null;
            case ProcessStatusConstant.WAIT_GETCAR:
                return UseCarTemlateEnum.WAIT_GETCAR;
            case ProcessStatusConstant.WAIT_RETURNCAR:
                return null;
            case ProcessStatusConstant.WAIT_STA:
                return null;
            case ProcessStatusConstant.WAIT_END:
                return null;
            case ProcessStatusConstant.WAIT_RECIVE:
                return UseCarTemlateEnum.WAIT_RECIVE;
            case ProcessStatusConstant.RECIVE_BACK:
                return UseCarTemlateEnum.RECIVE_BACK;
        }

        return null;
    }
}
