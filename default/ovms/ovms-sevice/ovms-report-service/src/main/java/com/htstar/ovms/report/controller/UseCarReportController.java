package com.htstar.ovms.report.controller;

import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.report.api.req.ByCarReportPageReq;
import com.htstar.ovms.report.api.req.ByMonthReportReq;
import com.htstar.ovms.report.api.vo.MonthReportVO;
import com.htstar.ovms.report.api.vo.RespMonthReportVO;
import com.htstar.ovms.report.api.vo.UseByCarVO;
import com.htstar.ovms.report.service.UseCarReportSevice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Description: 用车报表
 * Author: flr
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:
 */
@RestController
@AllArgsConstructor
@RequestMapping("/useCar")
@Api(value = "用车报表", tags = "用车报表")
public class UseCarReportController {

    @Autowired
    private UseCarReportSevice useCarReportSevice;

    @PostMapping("/monthReport")
    @ApiOperation(value = "按月统计用车", notes = "按月统计用车")
    public R<RespMonthReportVO> monthReport(@Valid @RequestBody ByMonthReportReq req) {
        return useCarReportSevice.monthReport(req);
    }

    @PostMapping("/carReport")
    @ApiOperation(value = "按车辆统计用车", notes = "按车辆统计用车")
    public R<UseByCarVO> carReport(@Valid @RequestBody ByCarReportPageReq req) {
        return useCarReportSevice.carReport(req);
    }
}
