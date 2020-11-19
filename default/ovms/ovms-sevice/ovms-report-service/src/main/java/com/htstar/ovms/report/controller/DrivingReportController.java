package com.htstar.ovms.report.controller;

import com.htstar.ovms.common.core.util.R;

import com.htstar.ovms.report.api.req.ByCarReportPageReq;
import com.htstar.ovms.report.api.req.ByDrivingReportPageReq;
import com.htstar.ovms.report.api.req.DrivingReportReq;
import com.htstar.ovms.report.api.vo.DrivingReportVO;
import com.htstar.ovms.report.api.vo.UseByCarVO;
import com.htstar.ovms.report.api.vo.VceDrivingReportlVO;
import com.htstar.ovms.report.api.vo.VceTotalPageVO;
import com.htstar.ovms.report.service.DrivingReportSevice;
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
 * @author JinZhu
 * @Description:
 * @date 2020/8/319:36
 */
@RestController
@AllArgsConstructor
@RequestMapping("/drivingRet")
@Api(value = "行驶报表", tags = "行驶报表")
public class DrivingReportController {

    @Autowired
    private DrivingReportSevice reportSevice;

    @PostMapping("/monthReport")
    @ApiOperation(value = "按月统计行驶记录", notes = "按月统计行驶记录")
    public R<List<DrivingReportVO>> drivingReport(@Valid @RequestBody DrivingReportReq req) {
        return  reportSevice.drivingReportI(req);
    }


    @PostMapping("/vceMonthReport")
    @ApiOperation(value = "按车辆统计行驶记录", notes = "按车辆统计行驶记录")
    public R<VceTotalPageVO> vceDrivingReport(@Valid @RequestBody ByDrivingReportPageReq req) {
        return reportSevice.vceDrivingReport(req);
  }
}
