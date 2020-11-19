package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.enterprise.api.entity.ApplyCarOrder;
import com.htstar.ovms.enterprise.api.entity.ApplyVerifyNode;
import com.htstar.ovms.enterprise.api.vo.VerifyUserVO;

import java.util.List;

/**
 * 审批节点
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
public interface ApplyVerifyNodeService extends IService<ApplyVerifyNode> {
    
    /**
     * Description: 获取nodeId
     * Author: flr
     * Date: 2020/7/2 14:39
     * Company: 航通星空
     * Modified By: 
     */
    Integer getNodeId(Integer processId, int nodeType);
    ApplyVerifyNode getNode(Integer processId, int nodeType);
    /**
     * Description: 获取审批人员列表(*****参数都是未来时的*****)
     * Author: flr
     * Date: 2020/7/7 15:07
     * Company: 航通星空
     * Modified By:
     */
    List<Integer> queryVerifyUserId(ApplyCarOrder order);
    List<VerifyUserVO> queryVerifyUserVOByStr(String verifyUserList);
    List<VerifyUserVO> getVerifyUserVOList(ApplyVerifyNode node);

    String getNickName(Integer applyUserId);
}
