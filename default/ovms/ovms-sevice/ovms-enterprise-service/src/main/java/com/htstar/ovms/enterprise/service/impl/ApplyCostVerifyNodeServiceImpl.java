package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.constant.CostVerifyTypeConstant;
import com.htstar.ovms.enterprise.api.entity.ApplyCostVerifyNode;
import com.htstar.ovms.enterprise.api.vo.VerifyUserVO;
import com.htstar.ovms.enterprise.mapper.ApplyCostVerifyNodeMapper;
import com.htstar.ovms.enterprise.service.ApplyCostProcessRecordService;
import com.htstar.ovms.enterprise.service.ApplyCostVerifyNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 费用审批配置
 *
 * @author lw
 * @date 2020-07-14 10:59:15
 */
@Service
@Slf4j
public class ApplyCostVerifyNodeServiceImpl extends ServiceImpl<ApplyCostVerifyNodeMapper, ApplyCostVerifyNode> implements ApplyCostVerifyNodeService {
    @Autowired
    private ApplyCostProcessRecordService applyCostProcessRecordService;
    /**
     * 获取最新的配置信息
     *
     * @return
     */
    @Override
    public ApplyCostVerifyNode getEtpNowCostVerifyNode() {
        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();
        //获取最新的配置信息
        ApplyCostVerifyNode verifyNode = baseMapper.selectOne(new QueryWrapper<ApplyCostVerifyNode>()
                .eq("etp_id", etpId));
        if (verifyNode==null){
            verifyNode=this.createApplyNode();
        }
        //需要审批
        if (verifyNode.getVerifyType()==1){
            List<VerifyUserVO> verifyUserVOList= baseMapper.getVerifyUserVOList(verifyNode.getVerifyUserList());
            verifyNode.setVerifyUserVOList(verifyUserVOList);
        }

        return verifyNode;
    }

    /**
     * 创建一个审批配置
     *
     * @return
     */
    @Override
    public ApplyCostVerifyNode createApplyNode() {
        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();
        ApplyCostVerifyNode applyCostVerifyNode = new ApplyCostVerifyNode();
        applyCostVerifyNode.setEtpId(etpId);
        applyCostVerifyNode.setVerifyType(CostVerifyTypeConstant.FREE_VERIFY);
        applyCostVerifyNode.setCreateUserId(user.getId());
        baseMapper.insert(applyCostVerifyNode);
        return applyCostVerifyNode;
    }


    /**
     * 保存配置信息
     *
     * @param costVerifyNode
     * @return
     */
    @Override
    public R saveCostVerifyNode(ApplyCostVerifyNode costVerifyNode) {
        log.info("审批结果为{}",costVerifyNode );
        //审批人
        String verifyUserList = costVerifyNode.getVerifyUserList();
        //是否需要审批
        Integer verifyType = costVerifyNode.getVerifyType();
        OvmsUser user = SecurityUtils.getUser();
        Integer userId = user.getId();
        Integer etpId = user.getEtpId();
        //不需要审批,判断当前是否有未审批完的
        if (verifyType == CostVerifyTypeConstant.FREE_VERIFY) {
            Boolean noApproved = applyCostProcessRecordService.isNoApproved(etpId);
            if (noApproved){
                return R.failed("有待审批费用,无法更改配置");
            }
        }
        //需要审核
        if (verifyType == CostVerifyTypeConstant.NEED_VERIFY) {
            if (StrUtil.isEmpty(verifyUserList)) {
                return R.failed("请设置审核人员");
            }
        }
        costVerifyNode.setCreateUserId(userId);
        costVerifyNode.setVerifyUserList(verifyUserList);
        this.updateById(costVerifyNode);
        return R.ok();
    }

    /**
     * 判断是否需要审批
     *
     * @return
     */
    @Override
    public Boolean isNeedVerify() {
        ApplyCostVerifyNode etpNowCostVerifyNode = this.getEtpNowCostVerifyNode();
        //无需审批
        if (etpNowCostVerifyNode.getVerifyType() == null || etpNowCostVerifyNode.getVerifyType() == 0) {
            return false;
        }
        return true;
    }




    /**
     * 判断是否能够修改
     *
     * @param
     * @return
     */
    @Override
    public Boolean isUpdateItem() {
        OvmsUser user = SecurityUtils.getUser();
        Integer userId = user.getId();
        Integer etpId = user.getEtpId();
        //获取费用的所有审批人
        ApplyCostVerifyNode verifyNode = baseMapper.selectOne(new QueryWrapper<ApplyCostVerifyNode>().eq("etp_id", etpId));
        //没有配置审批
        if (verifyNode==null){
            verifyNode = this.createApplyNode();
        }
        String verifyUsers = verifyNode.getVerifyUserList();
        if (StrUtil.isEmpty(verifyUsers)){
            return false;
        }
        String[] tag = verifyUsers.split(",");
        List<String> list = Arrays.asList(tag);
        //修改的操作用户不在审批人列表 没有操作权限
        if (!list.contains(userId.toString())) {
            return false;
        }
        return true;
    }
}
