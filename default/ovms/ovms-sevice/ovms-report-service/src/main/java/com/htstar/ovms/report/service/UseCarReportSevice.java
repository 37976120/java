package com.htstar.ovms.report.service;

import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.report.api.req.ByCarReportPageReq;
import com.htstar.ovms.report.api.req.ByCarReportReq;
import com.htstar.ovms.report.api.req.ByMonthReportReq;
import com.htstar.ovms.report.api.vo.MonthReportVO;
import com.htstar.ovms.report.api.vo.RespMonthReportVO;
import com.htstar.ovms.report.api.vo.UseByCarVO;

import java.util.List;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:
 */
public interface UseCarReportSevice {
    R<RespMonthReportVO> monthReport(ByMonthReportReq req);

    R<UseByCarVO> carReport(ByCarReportPageReq req);
}
