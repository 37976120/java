package com.htstar.ovms.report.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.report.api.entity.ReportDriverExpense;
import com.htstar.ovms.report.api.entity.ReportExpense;
import com.htstar.ovms.report.api.req.CarReportReq;
import com.htstar.ovms.report.api.req.CostMonthReq;
import com.htstar.ovms.report.service.ReportDriverExpenseService;
import com.htstar.ovms.report.service.ReportExpenseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * @Author: lw
 * Date: Created in 2020/7/28
 * Company: 航通星空
 * Modified By:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cost")
@Api(value = "费用报表", tags = "费用报表")
public class ReportExpenseController {
    @Autowired
    private ReportExpenseService reportExpenseService;
    @Autowired
    private ReportDriverExpenseService driverExpenseService;
    @PostMapping("/monthTotal")
    @ApiOperation(value = "按月统计总费用", notes = "按月统计总费用")
    public R<ReportExpense> getTotalCostByMonth(@RequestBody CostMonthReq req){
        return reportExpenseService.getTotalCostByMonth(req);
    }

    @PostMapping("/monthTable")
    @ApiOperation(value = "按月统计报表", notes = "按月统计报表")
    public R<List<ReportExpense>> getCostTableByMonth(@RequestBody CostMonthReq req){
        return reportExpenseService.getCostTableByMonth(req);
    }

    @PostMapping("/carTotal")
    @ApiOperation(value = "车辆统计总费用", notes = "车辆统计总费用")
    public R<ReportExpense> getTotalCostByCar(@RequestBody CarReportReq req){
        return reportExpenseService.getTotalCostByCar(req);
    }

    @PostMapping("/carTable")
    @ApiOperation(value = "车辆统计总费报表", notes = "车辆统计总费报表")
    public R<IPage<ReportExpense>> getCostPageByCar(@RequestBody CarReportReq req){
        return reportExpenseService.getCostPageByCar(req);
    }


    @PostMapping("/driverTotal")
    @ApiOperation(value = "按司机统计总费用", notes = "按司机统计总费用")
    public R<ReportDriverExpense> getTotalCostByDriver(@RequestBody CarReportReq req){
        return R.ok(driverExpenseService.getTotalCostByDriver(req));
    }

    @PostMapping("/driverPage")
    @ApiOperation(value = "司机统计分页", notes = "司机统计分页")
    public R<IPage<ReportDriverExpense>> getCostPageByDriver(@RequestBody CarReportReq req){
        return driverExpenseService.getCostPageByDriver(req);
    }
}
