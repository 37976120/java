package com.htstar.ovms.report.service;

import com.htstar.ovms.common.core.util.R;

import com.htstar.ovms.report.api.req.ByDrivingReportPageReq;
import com.htstar.ovms.report.api.req.DrivingReportReq;
import com.htstar.ovms.report.api.req.PersonalReportReq;
import com.htstar.ovms.report.api.vo.DrivingReportVO;
import com.htstar.ovms.report.api.vo.VceDrivingReportlVO;
import com.htstar.ovms.report.api.vo.VceTotalPageVO;

import java.util.List;

/**
 * Description:
 * Author: JinZhu
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:  行驶报表
 */
public interface DrivingReportSevice {
    //按月统计行驶记录
    R<List<DrivingReportVO>> drivingReportI(DrivingReportReq req);
    //按车统计行驶记录
    R<VceTotalPageVO> vceDrivingReport(ByDrivingReportPageReq req);
    //按人员计行驶记录
    R<VceTotalPageVO> drivceMonthReport(ByDrivingReportPageReq req);
}
