package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.OvmListUtil;
import com.htstar.ovms.enterprise.api.constant.*;
import com.htstar.ovms.enterprise.api.entity.ApplyCarOrder;
import com.htstar.ovms.enterprise.api.entity.ApplyCarProcess;
import com.htstar.ovms.enterprise.api.entity.ApplyOrderTask;
import com.htstar.ovms.enterprise.api.entity.ApplyVerifyNode;
import com.htstar.ovms.enterprise.handle.event.ApplyCarOrderPushMsgEvent;
import com.htstar.ovms.enterprise.mapper.ApplyEventTaskMapper;
import com.htstar.ovms.enterprise.service.ApplyCarProcessService;
import com.htstar.ovms.enterprise.service.ApplyOrderTaskService;
import com.htstar.ovms.enterprise.service.ApplyVerifyNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批任务
 *
 * @author flr
 * @date 2020-07-01 09:25:22
 */
@Service
@Slf4j
public class ApplyOrderTaskServiceImpl extends ServiceImpl<ApplyEventTaskMapper, ApplyOrderTask> implements ApplyOrderTaskService {

    @Autowired
    private ApplyVerifyNodeService nodeService;

    @Autowired
    private ApplyCarProcessService processService;

    /**
     *  上下文对象
     */
    @Autowired
    private ApplicationEventPublisher publisher;


    /**
     * Description: 生成任务（不关注流程）
     * Author: flr
     * Date: 2020/7/4 11:50
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public void createTask(ApplyCarOrder order) {
        List<Integer> list = new ArrayList<>();
        switch (order.getProcessStatus().intValue()){
            case ProcessStatusConstant.WAIT_VERIFY:
                if (StrUtil.isNotBlank(order.getVerifyUserList())){
                    list = OvmListUtil.splitStrList(order.getVerifyUserList());
                }else {
                    ApplyVerifyNode node =  nodeService.getNode(order.getProcessId(),order.getNextNodeType());
                    if (StrUtil.isNotBlank(node.getVerifyUserList())){
                        list = OvmListUtil.splitStrList(node.getVerifyUserList());
                    }else {
                        //按角色生成任务....
                        break;
                    }
                }
                break;
            case ProcessStatusConstant.WAIT_DISTRIBUTION:
                //分配：如果交车环节有配置，就是交车环节的审批人员，没配就是管理员
                ApplyVerifyNode dnode = nodeService.getNode(order.getProcessId(), ProcessNodeTypeConstant.GIVE_CAR);
                if (StrUtil.isNotBlank(dnode.getVerifyUserList())){
                    list = OvmListUtil.splitStrList(dnode.getVerifyUserList());
                }
                break;
            case ProcessStatusConstant.WAIT_GIVECAR:
                // 默认 是管理员
                ApplyVerifyNode gnode = nodeService.getNode(order.getProcessId(), ProcessNodeTypeConstant.GIVE_CAR);
                if (StrUtil.isNotBlank(gnode.getVerifyUserList())){
                    list = OvmListUtil.splitStrList(gnode.getVerifyUserList());
                }
                break;
            case ProcessStatusConstant.WAIT_GETCAR:
                //查看是否配置由分配司机提车
                //****自驾的话必须是申请人
                if (order.getDriveType() == ProcessOrderDriveTypeEnum.SELF.getCode()){
                    list.add(order.getApplyUserId());
                }else {
                    ApplyCarProcess process = processService.getById(order.getProcessId());
                    if (process.getDriverGetCarStatus().intValue() == 1){
                        if (null != order.getDriverId()){
                            list.add(order.getDriverId());
                        }
                    }else {
                        list.add(order.getApplyUserId());
                        if (null != order.getDriverId()){
                            list.add(order.getDriverId());
                        }
                    }
                }
                break;
            case ProcessStatusConstant.WAIT_RETURNCAR:
                //****自驾的话必须是申请人
                if (order.getDriveType() == ProcessOrderDriveTypeEnum.SELF.getCode()){
                    list.add(order.getApplyUserId());
                }else {
                    //申请人或者司机
                    list.add(order.getApplyUserId());
                    if (null != order.getDriverId()){
                        list.add(order.getDriverId());
                    }
                }
                break;
            case ProcessStatusConstant.WAIT_STA:
                list.add(order.getApplyUserId());
                break;
            case ProcessStatusConstant.WAIT_END:
                list.add(order.getApplyUserId());
                break;
            case ProcessStatusConstant.WAIT_RECIVE:
                // 默认 是管理员
                ApplyVerifyNode rnode = nodeService.getNode(order.getProcessId(), ProcessNodeTypeConstant.FINISH);
                if (StrUtil.isNotBlank(rnode.getVerifyUserList())){
                    list = OvmListUtil.splitStrList(rnode.getVerifyUserList());
                }
                break;
            case ProcessStatusConstant.RECIVE_BACK:
                ApplyVerifyNode node = nodeService.getNode(order.getProcessId(),order.getNextNodeType());
                if (node.getNodeType().intValue() == ProcessNodeTypeConstant.END_USE){
                    list.add(order.getApplyUserId());
                }else if (node.getNodeType().intValue() == ProcessNodeTypeConstant.RETURN_CAR){
                    //****自驾的话必须是申请人
                    if (order.getDriveType() == ProcessOrderDriveTypeEnum.SELF.getCode()){
                        list.add(order.getApplyUserId());
                    }else {
                        //申请人或者司机
                        list.add(order.getApplyUserId());
                        if (null != order.getDriverId()){
                            list.add(order.getDriverId());
                        }
                    }
                }
                break;
        }
        List<ApplyOrderTask> applyEventTaskList = new ArrayList<>();
        if (list.isEmpty()){
            //获取该企业下的管理员ID
            List<Integer> adlist = baseMapper.getAdminRoleId(order.getEtpId());
            if(null == adlist || adlist.isEmpty()){
                throw new RuntimeException("当前企业没有管理员！,企业ID：" + order.getEtpId());
            }

            for (Integer roleId :adlist){
                ApplyOrderTask task = new ApplyOrderTask();
                task.setOrderId(order.getOrderId());//审批表ID
                task.setProcessStatus(order.getProcessStatus());//流程状态：1=等待审批；2=审批拒绝；3=等待分配；4=等待交车；5=交车拒绝；6=等待提车；7=提车拒绝；
                task.setVerifyRoleId(roleId);//set审批人角色
                task.setVerifyType(NodeVerifyTypeConstant.ROLE_TASK);//set审批任务类型
                task.setTaskType(ProcessTaskTypeConstant.WAIT_ME);//set审批类型
                applyEventTaskList.add(task);
            }

        }else {
            list.stream().distinct().forEach(
                    verifyUserId->{
                        ApplyOrderTask applyEventTask = new ApplyOrderTask();
                        applyEventTask.setOrderId(order.getOrderId());
                        applyEventTask.setTaskType(ProcessTaskTypeConstant.WAIT_ME);
                        applyEventTask.setVerifyUserId(verifyUserId);
                        applyEventTask.setVerifyType(NodeVerifyTypeConstant.USER_TASK);//审批类型:1=指定用户ID；2=角色类型；
                        applyEventTask.setProcessStatus(order.getProcessStatus());
                        applyEventTaskList.add(applyEventTask);
                    }
            );

        }
        this.saveBatch(applyEventTaskList);
        try {
            //消息推送
            ApplyCarOrderPushMsgEvent event = new ApplyCarOrderPushMsgEvent(this,order,applyEventTaskList);
            publisher.publishEvent(event);
        }catch (Exception e){
            log.error("用车审批消息推送异常",e.getMessage());
        }
    }

    /**
     * Description: 生成抄送记录
     * Author: flr
     * Date: 2020/7/7 14:33
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public void createCCTask(Integer orderId, String ccUserList) {
        List<ApplyOrderTask> applyOrderTasks = new ArrayList<>();
        String[] createCCTaskArray = ccUserList.split(",");
        if (createCCTaskArray.length <= 0){
            return;
        }
        for (String userIdStr : createCCTaskArray){
            ApplyOrderTask task = new ApplyOrderTask();
            task.setProcessStatus(ProcessStatusConstant.FINISH);
            task.setOrderId(orderId);
            task.setVerifyType(NodeVerifyTypeConstant.USER_TASK);
            task.setTaskType(ProcessTaskTypeConstant.CC_ME);
            task.setVerifyUserId(Integer.parseInt(userIdStr));
            applyOrderTasks.add(task);
        }
        this.saveBatch(applyOrderTasks);
    }

    @Override
    public ApplyOrderTask getTask(Integer userId, Integer orderId, int taskType) {
        return baseMapper.selectOne(Wrappers.<ApplyOrderTask>lambdaQuery()
                .eq(ApplyOrderTask::getDelFlag, CommonConstants.STATUS_NORMAL)
                .eq(ApplyOrderTask::getVerifyType,NodeVerifyTypeConstant.USER_TASK)
                .eq(ApplyOrderTask::getOrderId,orderId)
                .eq(ApplyOrderTask::getVerifyUserId,userId)
                .eq(ApplyOrderTask::getTaskType,taskType));

    }

    /**
     * Description: 节点有人已处理
     * Author: flr
     * Date: 2020/8/3 15:11
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public void dealHaveDo(ApplyCarOrder order, Integer userId) {
        //1.判别这个这个流程状态是角色审批还是指定审批
        ApplyOrderTask pxtask = baseMapper.selectOne(Wrappers.<ApplyOrderTask>lambdaQuery()
                .eq(ApplyOrderTask::getDelFlag, CommonConstants.STATUS_NORMAL)
                .eq(ApplyOrderTask::getVerifyType,NodeVerifyTypeConstant.ROLE_TASK)
                .eq(ApplyOrderTask::getOrderId,order.getOrderId())
                .eq(ApplyOrderTask::getProcessStatus,order.getProcessStatus())
                .eq(ApplyOrderTask::getTaskType,ProcessTaskTypeConstant.WAIT_ME));

        if (pxtask != null){
            //角色审批
            //1.修改审批状态为已处理
            ApplyOrderTask up1 = new ApplyOrderTask();
            up1.setId(pxtask.getId());
            up1.setTaskType(ProcessTaskTypeConstant.DONE);
            up1.setDelFlag(CommonConstants.STATUS_DEL);
            this.updateById(up1);
            //2.生成一条我已处理的
            ApplyOrderTask inent = new ApplyOrderTask();
            inent.setTaskType(ProcessTaskTypeConstant.DONE);
            inent.setOrderId(order.getOrderId());
            inent.setVerifyType(NodeVerifyTypeConstant.USER_TASK);
            inent.setVerifyUserId(userId);
            inent.setProcessStatus(order.getProcessStatus());
            this.save(inent);
        }else {
            //指定用户的审批
            //1.修改我的为已处理
//            log.info("修改我的为已处理",userId,order.getOrderId(),order.getProcessStatus(),ProcessTaskTypeConstant.DONE);
            baseMapper.updateByParams(userId,order.getOrderId(),order.getProcessStatus(),ProcessTaskTypeConstant.DONE);
            //2.删除其他人的审批任务
            baseMapper.delByParams(userId,order.getOrderId(),order.getProcessStatus());
        }
    }

    @Override
    public void overTask(Integer orderId) {
        baseMapper.overTask(orderId);
    }

    @Override
    public List<Integer> getVerifyIdList(Integer orderId) {
        return baseMapper.getVerifyIdList(orderId);
    }


    /**
     * Description: 清除所有任务
     * Author: flr
     * Date: 2020/8/3 14:07
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public void cleanAllTask(Integer orderId) {
        baseMapper.cleanAllTask(orderId);
    }
}
