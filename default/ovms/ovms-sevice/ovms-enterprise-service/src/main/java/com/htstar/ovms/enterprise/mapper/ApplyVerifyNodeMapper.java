package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.enterprise.api.entity.ApplyVerifyNode;
import com.htstar.ovms.enterprise.api.vo.VerifyUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审批节点
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
@Mapper
public interface ApplyVerifyNodeMapper extends BaseMapper<ApplyVerifyNode> {

    /**
     * Description: 获取nodeId
     * Author: flr
     * Date: 2020/7/2 14:39
     * Company: 航通星空
     * Modified By:
     */
    Integer getNodeId(@Param("processId") Integer processId, @Param("nodeType") int nodeType);

    ApplyVerifyNode getNextNeedVerifyNode(@Param("processId") Integer processId, @Param("nodeType") Integer nodeType);

    List<VerifyUserVO> getVerifyUserVOList(@Param("ids") String ids);


    String getNickName(@Param("userId") Integer userId);
}
