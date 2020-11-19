package com.htstar.ovms.report.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.report.api.entity.ReportExpense;
import com.htstar.ovms.report.api.req.CarReportReq;
import com.htstar.ovms.report.api.req.CostMonthReq;

import java.util.List;
import java.util.Map;

/**
 * 费用报表数据
 *
 * @author lw
 * @date 2020-07-27 15:19:34
 */
public interface ReportExpenseService extends IService<ReportExpense> {

    /**
     * 表
     * 按月份统计费用的总计费用
     * @param req
     * @return
     */
    R<ReportExpense> getTotalCostByMonth(CostMonthReq req);

    /**
     * 按月统计列表数据
     * @param req
     * @return
     */
    R<List<ReportExpense>> getCostTableByMonth(CostMonthReq req);


    /**
     * 按车辆统计费用当月总费用
     * @param req
     * @return
     */
    R<ReportExpense>  getTotalCostByCar(CarReportReq req);

    /**
     * 按车辆统计费用列表
     * @param req
     * @return
     */
    R<IPage<ReportExpense>> getCostPageByCar(CarReportReq req);


    /**
     * 年月份处理
     * @param req
     * @return
     */
    public String  getMonthShort(CarReportReq req);


    /**
     * 车辆信息导出excel统一接口
     * @param rows
     * @param
     * @param
     */
    void exportUtil(List<Map<String,Object>> rows, String fileName) ;
}
