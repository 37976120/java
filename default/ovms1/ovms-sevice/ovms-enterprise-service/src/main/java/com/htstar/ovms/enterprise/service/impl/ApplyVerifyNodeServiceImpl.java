package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.feign.SysUserRoleFeign;
import com.htstar.ovms.common.core.util.OvmListUtil;
import com.htstar.ovms.enterprise.api.constant.ProcessNodeTypeConstant;
import com.htstar.ovms.enterprise.api.constant.ProcessOrderDriveTypeEnum;
import com.htstar.ovms.enterprise.api.constant.ProcessStatusConstant;
import com.htstar.ovms.enterprise.api.entity.ApplyCarOrder;
import com.htstar.ovms.enterprise.api.entity.ApplyCarProcess;
import com.htstar.ovms.enterprise.api.entity.ApplyOrderTask;
import com.htstar.ovms.enterprise.api.entity.ApplyVerifyNode;
import com.htstar.ovms.enterprise.api.vo.VerifyUserVO;
import com.htstar.ovms.enterprise.mapper.ApplyVerifyNodeMapper;
import com.htstar.ovms.enterprise.service.ApplyCarProcessService;
import com.htstar.ovms.enterprise.service.ApplyOrderTaskService;
import com.htstar.ovms.enterprise.service.ApplyVerifyNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批节点
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
@Service
public class ApplyVerifyNodeServiceImpl extends ServiceImpl<ApplyVerifyNodeMapper, ApplyVerifyNode> implements ApplyVerifyNodeService {

    @Autowired
    private SysUserRoleFeign sysUserRoleFeign;

    @Autowired
    private ApplyCarProcessService processService;

    @Autowired
    private ApplyOrderTaskService taskService;

    /**
     * Description: 获取nodeId
     * Author: flr
     * Date: 2020/7/2 14:39
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public Integer getNodeId(Integer processId, int nodeType) {
        return baseMapper.getNodeId(processId,nodeType);
    }

    @Override
    public ApplyVerifyNode getNode(Integer processId, int nodeType) {
        return baseMapper.selectOne(Wrappers.<ApplyVerifyNode>lambdaQuery()
                .eq(ApplyVerifyNode :: getProcessId,processId)
                .eq(ApplyVerifyNode :: getNodeType,nodeType));
    }

    /**
     * Description: 获取审批人员列表
     * Author: flr
     * Date: 2020/7/7 15:07
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public List<Integer> queryVerifyUserId(ApplyCarOrder order) {
        List<Integer> list = taskService.getVerifyIdList(order.getOrderId());
        if (null != list && !list.isEmpty()){
            return list;
        }else {
            list = new ArrayList<>();
        }
        String verifyUserList;
        switch (order.getProcessStatus().intValue()){
            case ProcessStatusConstant.WAIT_VERIFY:
                if (StrUtil.isNotBlank(order.getVerifyUserList())){
                    verifyUserList = order.getVerifyUserList();
                }else {
                    verifyUserList = this.getNode(order.getProcessId(),order.getNextNodeType()).getVerifyUserList();
                }

                if (StrUtil.isNotBlank(verifyUserList)){
                    //如果不为空就直接返回
                    list = OvmListUtil.splitStrList(verifyUserList);
                }else {
                    list = sysUserRoleFeign.queryAdminIdList(order.getEtpId());
                }
                break;
            case ProcessStatusConstant.WAIT_DISTRIBUTION:
                //分配：如果交车环节有配置，就是交车环节的审批人员，没配就是管理员
                verifyUserList = this.getNode(order.getProcessId(), ProcessNodeTypeConstant.GIVE_CAR).getVerifyUserList();
                if (StrUtil.isNotBlank(verifyUserList)){
                    //如果不为空就直接返回
                    list = OvmListUtil.splitStrList(verifyUserList);
                }else {
                    list = sysUserRoleFeign.queryAdminIdList(order.getEtpId());
                }
                break;
            case ProcessStatusConstant.WAIT_GIVECAR:
                // 默认 是管理员
                verifyUserList = this.getNode(order.getProcessId(), ProcessNodeTypeConstant.GIVE_CAR).getVerifyUserList();
                if (StrUtil.isNotBlank(verifyUserList)){
                    //如果不为空就直接返回
                    list = OvmListUtil.splitStrList(verifyUserList);
                }else {
                    list = sysUserRoleFeign.queryAdminIdList(order.getEtpId());
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
                verifyUserList = this.getNode(order.getProcessId(), ProcessNodeTypeConstant.FINISH).getVerifyUserList();
                if (StrUtil.isNotBlank(verifyUserList)){
                    //如果不为空就直接返回
                    list = OvmListUtil.splitStrList(verifyUserList);
                }else {
                    list = sysUserRoleFeign.queryAdminIdList(order.getEtpId());
                }
                break;
            case ProcessStatusConstant.RECIVE_BACK:
                ApplyVerifyNode node = this.getNode(order.getProcessId(),order.getNextNodeType());
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
        return list;
    }


    @Override
    public List<VerifyUserVO> queryVerifyUserVOByStr(String verifyUserList) {
        return baseMapper.getVerifyUserVOList(verifyUserList);
    }

    @Override
    public List<VerifyUserVO> getVerifyUserVOList(ApplyVerifyNode node) {

        List<VerifyUserVO> userVOList = new ArrayList<>();

        if (StrUtil.isBlank(node.getVerifyUserList())){
            return userVOList;
        }else {
            return this.queryVerifyUserVOByStr(node.getVerifyUserList());
        }

//        switch (node.getNodeType().intValue()){
//            case ProcessNodeTypeConstant.APPLY:
//                break;
//            case ProcessNodeTypeConstant.VERIFY:
//                if (StrUtil.isBlank(node.getVerifyUserList())){
//                    // 默认 是管理员
//                    List<Integer> list = sysUserRoleFeign.queryAdminIdList(node.getEtpId());
//                    userVOList = this.queryVerifyUserVOByStr(StrUtil.join(",",list));
//
//                }else {
//                    userVOList = this.queryVerifyUserVOByStr(node.getVerifyUserList());
//                }
//                break;
//            case ProcessNodeTypeConstant.START_USE:
//                break;
//            case ProcessNodeTypeConstant.END_USE:
//                break;
//            case ProcessNodeTypeConstant.GIVE_CAR:
//                if (StrUtil.isBlank(node.getVerifyUserList())){
//                    // 默认 是管理员
//                    List<Integer> list = sysUserRoleFeign.queryAdminIdList(node.getEtpId());
//                    userVOList = this.queryVerifyUserVOByStr(StrUtil.join(",",list));
//
//                }else {
//                    userVOList = this.queryVerifyUserVOByStr(node.getVerifyUserList());
//                }
//                break;
//            case ProcessNodeTypeConstant.GET_CAR:
//                break;
//            case ProcessNodeTypeConstant.RETURN_CAR:
//                break;
//            case ProcessNodeTypeConstant.FINISH:
//                if (StrUtil.isBlank(node.getVerifyUserList())){
//                    // 默认 是管理员
//                    List<Integer> list = sysUserRoleFeign.queryAdminIdList(node.getEtpId());
//                    userVOList = this.queryVerifyUserVOByStr(StrUtil.join(",",list));
//
//                }else {
//                    userVOList = this.queryVerifyUserVOByStr(node.getVerifyUserList());
//                }
//                break;
//        }
//        return userVOList;
    }

    @Override
    public String getNickName(Integer applyUserId) {
        return baseMapper.getNickName(applyUserId);
    }

}
