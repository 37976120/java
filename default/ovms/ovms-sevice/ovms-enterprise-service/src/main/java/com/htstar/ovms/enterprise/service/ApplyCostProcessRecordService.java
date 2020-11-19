package com.htstar.ovms.enterprise.service;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.ApplyCostProcessRecord;
import com.htstar.ovms.enterprise.api.req.ApprovalRecordReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.req.WithdrawReq;
import com.htstar.ovms.enterprise.api.vo.ApplyCostProcessRecordVo;


/**
 * 费用审批记录
 *
 * @author lw
 * @date 2020-07-14 10:59:15
 */
public interface ApplyCostProcessRecordService extends IService<ApplyCostProcessRecord> {
    /**
     * 添加申请 审批记录
     * @param costProcessRecord
     * @return
     */
    R saveApply(ApplyCostProcessRecord costProcessRecord);


    /**
     * 审批记录分页
     * @param req
     * @return
     */
   R<IPage<ApplyCostProcessRecordVo>> queryPage(ApprovalRecordReq req);

    /**
     *
     * 获取申请记录
     * @param costId
     * @param costType
     * @return
     */
    ApplyCostProcessRecordVo getApplyRecordById(Integer costId,Integer costType);



    /**
     * 是否有未审批完的记录
     * @param etpId
     * @return
     */
    Boolean isNoApproved(Integer etpId);


    /**
     * 存档
     * @param id
     * @return
     */
     R filing(Integer id);

    /**
     * 退回
     * @param req
     * @return
     */
     R withdraw(WithdrawReq req);


    /**
     * 导出excel
     * @param req
     */
    void  exportExcel(ExportReq req);


    /**
     * 删除审批记录根据费用id 跟费用类型
     * @param costId
     * @param costType
     * @return
     */
    R delByCostIdAndCostType(Integer costId,Integer costType);
}
