package com.htstar.ovms.report.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.report.api.entity.ReportDriverExpense;
import com.htstar.ovms.report.api.req.CarReportReq;

/**
 * 司机费用

 *
 * @author lw
 * @date 2020-08-01 10:11:28
 */
public interface ReportDriverExpenseService extends IService<ReportDriverExpense> {
    /**
     * 按司机统计费用 计算总费用
     * @param carReportReq
     * @return
     */
    ReportDriverExpense getTotalCostByDriver(CarReportReq carReportReq);

    /**
     * 统计司机分页
     * @param carReportReq
     * @return
     */
    R<IPage<ReportDriverExpense>> getCostPageByDriver(CarReportReq carReportReq);
}
