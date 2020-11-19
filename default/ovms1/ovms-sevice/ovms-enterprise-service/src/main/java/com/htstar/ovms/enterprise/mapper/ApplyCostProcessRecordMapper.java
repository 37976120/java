package com.htstar.ovms.enterprise.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.enterprise.api.entity.ApplyCostProcessRecord;
import com.htstar.ovms.enterprise.api.req.ApplyCostProcessReq;
import com.htstar.ovms.enterprise.api.req.ApprovalRecordReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.ApplyCostProcessRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费用审批记录
 *
 * @author lw
 * @date 2020-07-14 10:59:15
 */
@Mapper
public interface ApplyCostProcessRecordMapper extends BaseMapper<ApplyCostProcessRecord> {
    /**
     * 获取审批记录 通过费用id和费用类型
     * @param costId
     * @param costType
     * @return
     */
    ApplyCostProcessRecordVo getApplyRecordById(@Param("costId") Integer costId, @Param("costType") Integer costType);

    /**
     * 审批分页
     * @param req
     * @return
     */
    IPage<ApplyCostProcessRecordVo> queryPage(@Param("req") ApprovalRecordReq req);

    List<ApplyCostProcessRecordVo> exportExcel(@Param("req") ExportReq req);

    /**
     * 删除审批记录根据费用id 跟费用类型
     * @param costId
     * @param costType
     */
    void delByCostIdAndCostType(@Param("costId") Integer costId,@Param("costType") Integer costType);

    /**
     * 根据用户ID获取昵称
     * @param userId
     * @return
     */
    String getNickName(@Param("userId") Integer userId);
}
