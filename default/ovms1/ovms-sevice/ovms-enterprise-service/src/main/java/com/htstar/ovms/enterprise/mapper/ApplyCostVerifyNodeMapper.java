package com.htstar.ovms.enterprise.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.enterprise.api.entity.ApplyCostVerifyNode;
import com.htstar.ovms.enterprise.api.vo.VerifyUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费用审批配置
 *
 * @author lw
 * @date 2020-07-14 10:59:15
 */
@Mapper
public interface ApplyCostVerifyNodeMapper extends BaseMapper<ApplyCostVerifyNode> {


    /**
     * 获取费用的所有审批人
     * @param etpId
     * @return
     */
    String getCostVerifyUsers(@Param("etpId") Integer etpId);

    /**
     * 获取审批人员信息
     * @param verifyUserList
     * @return
     */
    List<VerifyUserVO> getVerifyUserVOList(String verifyUserList);
}
