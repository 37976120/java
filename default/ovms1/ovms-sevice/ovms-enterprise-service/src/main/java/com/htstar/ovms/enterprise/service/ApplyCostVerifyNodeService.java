package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.ApplyCostVerifyNode;


/**
 * 费用审批配置
 *
 * @author lw
 * @date 2020-07-14 10:59:15
 */
public interface ApplyCostVerifyNodeService extends IService<ApplyCostVerifyNode> {

    /**
     * 获取企业最新审核配置
     * @return
     */
    ApplyCostVerifyNode getEtpNowCostVerifyNode();

    /**
     * 创建一个审批配置
     * @return
     */
    ApplyCostVerifyNode createApplyNode();


    /**
     * 保存配置信息
     * @param costVerifyNode
     * @return
     */
    R saveCostVerifyNode(ApplyCostVerifyNode costVerifyNode);


    /**
     * 判断是否需要审批
     * @return
     */
    Boolean isNeedVerify();



    /**
     * 判断是否能够修改
     * @param
     * @return
     */
    Boolean isUpdateItem();


}
