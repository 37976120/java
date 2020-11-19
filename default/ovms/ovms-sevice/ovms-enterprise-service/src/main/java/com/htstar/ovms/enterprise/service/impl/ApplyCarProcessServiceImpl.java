package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.constant.ProcessNodeTypeConstant;
import com.htstar.ovms.enterprise.api.constant.ProcessOrderDriveTypeEnum;
import com.htstar.ovms.enterprise.api.constant.ProcessTypeConstant;
import com.htstar.ovms.enterprise.api.entity.ApplyCarProcess;
import com.htstar.ovms.enterprise.api.entity.ApplyVerifyNode;
import com.htstar.ovms.enterprise.api.req.CarProcessReq;
import com.htstar.ovms.enterprise.api.req.SetNodeVerifyUserReq;
import com.htstar.ovms.enterprise.api.req.VerifyNodeReq;
import com.htstar.ovms.enterprise.api.vo.ApplyCarProcessVO;
import com.htstar.ovms.enterprise.api.vo.ApplyVerifyNodeVO;
import com.htstar.ovms.enterprise.api.vo.NodeVerifyUserVO;
import com.htstar.ovms.enterprise.api.vo.VerifyUserVO;
import com.htstar.ovms.enterprise.mapper.ApplyOfficeCarProcessMapper;
import com.htstar.ovms.enterprise.mapper.ApplyVerifyNodeMapper;
import com.htstar.ovms.enterprise.service.ApplyCarProcessService;
import com.htstar.ovms.enterprise.service.ApplyVerifyNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 公车申请流程
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
@Service
@Slf4j
public class ApplyCarProcessServiceImpl extends ServiceImpl<ApplyOfficeCarProcessMapper, ApplyCarProcess> implements ApplyCarProcessService {
    @Autowired
    private ApplyVerifyNodeService nodeService;

    @Autowired
    private ApplyVerifyNodeMapper nodeMapper;

    /**
     * Description:用车申请流程配置
     * Author: flr
     * Company: 航通星空
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R configOfficeCarProcess(CarProcessReq req) {
        if (req.getProcessType() == null){
            return R.failed("请求参数错误，请联系管理员！");
        }
        OvmsUser user = SecurityUtils.getUser();
        if (!SecurityUtils.getRoleCode(user).contains(CommonConstants.ROLE_ADMIN)){
            R.failed("只有企业管理员可以操作！");
        }

        Integer etpId = user.getEtpId();
        List<VerifyNodeReq> verifyNodeVOList = req.getVerifyNodeList();
        if (null == verifyNodeVOList || verifyNodeVOList.isEmpty()){
            return R.failed("请求参数错误，请联系管理员！");
        }

        if (verifyNodeVOList.size() <= 3){
            return R.failed("至少要有3个节点！");
        }

        ApplyCarProcess applyOfficeCarProcess = new ApplyCarProcess();
        applyOfficeCarProcess.setEtpId(etpId);
        applyOfficeCarProcess.setProcessType(req.getProcessType());
        applyOfficeCarProcess.setDriverGetCarStatus(req.getDriverGetCarStatus());
        applyOfficeCarProcess.setMileageStatus(req.getMileageStatus());

        this.save(applyOfficeCarProcess);

        Integer processId = applyOfficeCarProcess.getId();
        if (processId == null){
            throw new RuntimeException("插入节点时未获取到自增主键");
        }

        String nodeIdListStr = "";
        for(VerifyNodeReq vo : verifyNodeVOList){
            ApplyVerifyNode nodeEnt = new ApplyVerifyNode();
            BeanUtil.copyProperties(vo,nodeEnt);
            nodeEnt.setEtpId(etpId);
            nodeEnt.setProcessId(processId);
            nodeEnt.setCreateUserId(user.getId());
            nodeService.save(nodeEnt);

            if (nodeEnt.getId() == null){
                throw new RuntimeException("插入节点时未获取到自增主键");
            }
            nodeIdListStr += nodeEnt.getId() + ",";
        }

        //去掉最后一个逗号
        nodeIdListStr = nodeIdListStr.substring(0,nodeIdListStr.length() -1);

        applyOfficeCarProcess.setVerifyNodeList(nodeIdListStr);

        this.updateById(applyOfficeCarProcess);

        return R.ok();
    }

    /**
     * Description: 得到企业当前运行的公车申请流程配置
     * Author: flr
     * Date: 2020/7/1 14:29
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public ApplyCarProcess getNowRunOfficeProcess(Integer etpId) {
        ApplyCarProcess process = baseMapper.getNowRunProcess(etpId, ProcessTypeConstant.PUBLIC_APPLY);
        if (null == process){
            //不存在配置，给企业添加一个默认公车申请配置
            process = this.createDefualtOfficeCarProcess(etpId);
        }
        return process;
    }

    /**
     * Description: 得到企业当前运行的私车公用申请流程配置
     * Author: flr
     * Date: 2020/7/4 17:35
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public ApplyCarProcess getNowRunPrivateProcess(Integer etpId) {
        ApplyCarProcess process = baseMapper.getNowRunProcess(etpId, ProcessTypeConstant.PRIVATE_APPLY);
        if (null == process){
            //不存在配置，给企业添加一个默认私车公用申请配置
            process = this.createDefualtPrivateCarProcess(etpId);
        }
        return process;
    }

    private ApplyCarProcess createDefualtPrivateCarProcess(Integer etpId) {
        ApplyCarProcess defualtProcess = new ApplyCarProcess();
        defualtProcess.setProcessType(ProcessTypeConstant.PRIVATE_APPLY);
        defualtProcess.setEtpId(etpId);
        defualtProcess.setMileageStatus(1);
        save(defualtProcess);

        Integer processId = defualtProcess.getId();
        if (processId == null){
            throw new RuntimeException("插入节点时未获取到自增主键");
        }

        String nodeIdListStr = "";
        for (int i = 0; i < 5; i++) {//默认的5个流程
            ApplyVerifyNode nodeEnt = new ApplyVerifyNode();
            nodeEnt.setEtpId(etpId);
            nodeEnt.setProcessId(processId);
            //节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；31=开始用车（私）；40=提车；50=还车；51=结束用车（私）；60=完成；
            if (i == 0){
                nodeEnt.setNodeType(ProcessNodeTypeConstant.APPLY);
            }else if (i == 1){
                nodeEnt.setNodeType(ProcessNodeTypeConstant.GIVE_CAR);
            }else if (i == 2){
                nodeEnt.setNodeType(ProcessNodeTypeConstant.START_USE);
            }else if (i == 3){
                nodeEnt.setNodeType(ProcessNodeTypeConstant.END_USE);
            }else if (i == 4){
                nodeEnt.setNodeType(ProcessNodeTypeConstant.FINISH);
            }
            nodeService.save(nodeEnt);
            if (nodeEnt.getId() == null){
                throw new RuntimeException("插入节点时未获取到自增主键");
            }
            nodeIdListStr += nodeEnt.getId() + ",";
        }
        //去掉最后一个逗号
        nodeIdListStr = nodeIdListStr.substring(0,nodeIdListStr.length() -1);
        defualtProcess.setVerifyNodeList(nodeIdListStr);
        this.updateById(defualtProcess);
        return defualtProcess;
    }

    /**
     * Description: 给企业添加一个默认公车申请配置
     * Author: flr
     * Date: 2020/7/1 14:42
     * Company: 航通星空
     * Modified By:
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApplyCarProcess createDefualtOfficeCarProcess(Integer etpId) {
        ApplyCarProcess defualtProcess = new ApplyCarProcess();
        defualtProcess.setProcessType(ProcessTypeConstant.PUBLIC_APPLY);
        defualtProcess.setEtpId(etpId);
        save(defualtProcess);

        Integer processId = defualtProcess.getId();
        if (processId == null){
            throw new RuntimeException("插入节点时未获取到自增主键");
        }
        String nodeIdListStr = "";
        for (int i = 0; i < 5; i++) {//默认的5个流程
            ApplyVerifyNode nodeEnt = new ApplyVerifyNode();
            nodeEnt.setEtpId(etpId);
            nodeEnt.setProcessId(processId);
            //节点类型：10=申请；20=审批；30=公车（交车）,私车（分配司机）；40=提车；50=还车；60=完成；
            if (i == 0){
                nodeEnt.setNodeType(ProcessNodeTypeConstant.APPLY);
            }else if (i == 1){
                nodeEnt.setNodeType(ProcessNodeTypeConstant.GIVE_CAR);
            }else if (i == 2){
                nodeEnt.setNodeType(ProcessNodeTypeConstant.GET_CAR);
            }else if (i == 3){
                nodeEnt.setNodeType(ProcessNodeTypeConstant.RETURN_CAR);
            }else if (i == 4){
                nodeEnt.setNodeType(ProcessNodeTypeConstant.FINISH);
            }
            nodeService.save(nodeEnt);
            if (nodeEnt.getId() == null){
                throw new RuntimeException("插入节点时未获取到自增主键");
            }
            nodeIdListStr += nodeEnt.getId() + ",";
        }

        //去掉最后一个逗号
        nodeIdListStr = nodeIdListStr.substring(0,nodeIdListStr.length() -1);
        defualtProcess.setVerifyNodeList(nodeIdListStr);
        this.updateById(defualtProcess);
        return defualtProcess;
    }

    /**
     * Description: 获取前一步的节点
     * Author: flr
     * Date: 2020/7/4 9:35
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public ApplyVerifyNode getBeforeNode(Integer processId, Integer nodeType) {
        List<ApplyVerifyNode> verifyNodeList = getVerifyNodeList(processId);
        for (int i = 0; i < verifyNodeList.size(); i++) {
            if (verifyNodeList.get(i).getNodeType().intValue() == nodeType && i > 0){
                return verifyNodeList.get(i-1);
            }
        }
        return null;
    }

    /**
     * Description: 得到流程的节点列表（有序）
     * Author: flr
     * Date: 2020/7/3 11:05
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public List<ApplyVerifyNode> getVerifyNodeList(Integer processId) {
        return nodeMapper
                .selectList(Wrappers.<ApplyVerifyNode>lambdaQuery()
                        .eq(ApplyVerifyNode::getProcessId,processId)
                        .orderByAsc(ApplyVerifyNode::getNodeType));
    }

    private List<ApplyVerifyNode> getVerifyNodeListNohaveGiveCar(int processId) {
        return nodeMapper
                .selectList(Wrappers.<ApplyVerifyNode>lambdaQuery()
                        .eq(ApplyVerifyNode::getProcessId,processId)
                        .notIn(ApplyVerifyNode::getNodeType,ProcessNodeTypeConstant.GIVE_CAR)
                        .orderByAsc(ApplyVerifyNode::getNodeType));
    }

    /**
     * Description: 详情
     * Author: flr
     * Date: 2020/7/7 14:49
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R<ApplyCarProcessVO> getVoById(Integer id) {
        ApplyCarProcess process = this.getById(id);
        if (null == process){
            return R.failed("不存在的流程，id" + id);
        }
        ApplyCarProcessVO vo = new ApplyCarProcessVO();
        BeanUtil.copyProperties(process,vo);
        List<ApplyVerifyNode> verifyNodeList = this.getVerifyNodeList(id);
        List<ApplyVerifyNodeVO> verifyNodeVOList = new ArrayList<>();
        if(null != verifyNodeList &&!verifyNodeList.isEmpty()){
            ApplyVerifyNodeVO nodeVO = new ApplyVerifyNodeVO();
            for (ApplyVerifyNode node : verifyNodeList){
                BeanUtil.copyProperties(node,nodeVO);
                List<VerifyUserVO> userVOS = nodeService.getVerifyUserVOList(node);
                nodeVO.setVerifyUserVoList(userVOS);
                verifyNodeVOList.add(nodeVO);
            }

        }
        vo.setApplyVerifyNodeList(verifyNodeVOList);
        return R.ok(vo);
    }

    /**
     * Description: 企业获取当前流程
     * Author: flr
     * Date: 2020/7/9 13:48
     * Company: 航通星空
     * Modified By:
     */
    @Override
    @Transactional
    public R<ApplyCarProcessVO> getEtpNowProcess(Integer processType) {
        OvmsUser user = SecurityUtils.getUser();
        int etpId = user.getEtpId();
        if (null == processType){
            return R.failed("请正确传参！");
        }

        ApplyCarProcess process = null;
        //流程类型:0=公车申请；1=私车公用；
        switch (processType){
            case 0:
                process = getNowRunOfficeProcess(etpId);
                break;
            case 1:
                process = getNowRunPrivateProcess(etpId);
                break;
        }

        if (null == process){
            return R.failed("为获取到企业配置流程，请联系管理员！");
        }
        ApplyCarProcessVO vo = new ApplyCarProcessVO();
        BeanUtil.copyProperties(process,vo);
        List<ApplyVerifyNode> verifyNodeList = this.getVerifyNodeList(process.getId());
        List<ApplyVerifyNodeVO> verifyNodeVOList = new ArrayList<>();
        if(null != verifyNodeList &&!verifyNodeList.isEmpty()){
            for (ApplyVerifyNode node : verifyNodeList){
                ApplyVerifyNodeVO nodeVO = new ApplyVerifyNodeVO();
                BeanUtil.copyProperties(node,nodeVO);
                List<VerifyUserVO> userVOS = nodeService.getVerifyUserVOList(node);
                nodeVO.setVerifyUserVoList(userVOS);
                verifyNodeVOList.add(nodeVO);
            }

        }
        vo.setApplyVerifyNodeList(verifyNodeVOList);
        return R.ok(vo);
    }

    /**
     * Description:获取节点的审批人员
     * Author: flr
     * Date: 2020/7/9 15:17
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public R<NodeVerifyUserVO> getNodeVerifyUser(Integer nodeId) {
        ApplyVerifyNode node = nodeService.getById(nodeId);
        if (node == null){
            return R.failed("节点不存在，节点ID:" + nodeId);
        }
        NodeVerifyUserVO vo = new NodeVerifyUserVO();
        List<VerifyUserVO> verifyUserVOList = nodeService.getVerifyUserVOList(node);
        vo.setVerifyUserVOList(verifyUserVOList);
        return R.ok(vo);
    }

    /**
     * Description: 设置节点的审批人员
     * Author: flr
     * Date: 2020/7/11 9:31
     * Company: 航通星空
     * Modified By:
     */
    @Override
    @Transactional
    public R setNodeVerifyUser(SetNodeVerifyUserReq req) {
        if (null == req.getNodeId() || req.getVerifyUserList() == null){
            return R.failed("请正确传参！");
        }

        ApplyVerifyNode node = nodeService.getById(req.getNodeId());
        ApplyCarProcess process = this.getById(node.getProcessId());
        ApplyCarProcess insetProcess = new ApplyCarProcess();
        insetProcess.setMileageStatus(process.getMileageStatus());
        insetProcess.setDriverGetCarStatus(process.getDriverGetCarStatus());
        insetProcess.setProcessType(process.getProcessType());
        insetProcess.setEtpId(process.getEtpId());
        this.save(insetProcess);

        List<ApplyVerifyNode> verifyNodeList = this.getVerifyNodeList(process.getId());
        String nodeIdListStr = "";

        int result = 0;
        for (ApplyVerifyNode ex :verifyNodeList){
            ApplyVerifyNode ent = new ApplyVerifyNode();
            if (ex.getId().intValue() == req.getNodeId().intValue()){
                ex.setVerifyUserList(req.getVerifyUserList());
                result = 1;
            }
            ex.setId(null);
            BeanUtil.copyProperties(ex,ent);
            ent.setProcessId(insetProcess.getId());
            nodeService.save(ent);
            if (result == 1){
                result = ent.getId();
            }

            nodeIdListStr = nodeIdListStr + ent.getId() + ",";
        }
        //去掉最后一个逗号
        nodeIdListStr = nodeIdListStr.substring(0,nodeIdListStr.length() -1);
        if (StrUtil.isBlank(nodeIdListStr)){
            insetProcess.setVerifyNodeList(null);
        }
        insetProcess.setVerifyNodeList(nodeIdListStr);
        this.updateById(insetProcess);
        return R.ok(result);
    }

    /**
     * Description: 获取下个节点，私车自驾屏蔽交车环节
     * Author: flr
     * Date: 2020/8/3 13:59
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public ApplyVerifyNode getNextNode(int applyType, int driveType, int processId, int nodeType) {
        List<ApplyVerifyNode> verifyNodeList;
        if (applyType == ProcessTypeConstant.PRIVATE_APPLY && driveType == ProcessOrderDriveTypeEnum.SELF.getCode()){
            verifyNodeList = getVerifyNodeListNohaveGiveCar(processId);
        }else {
            verifyNodeList = getVerifyNodeList(processId);
        }
        for (ApplyVerifyNode node : verifyNodeList){
            if (node.getNodeType().intValue() > nodeType){
                return node;
            }
        }

        return null;
    }




}
