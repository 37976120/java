package com.htstar.ovms.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.report.api.entity.ReportExpense;
import com.htstar.ovms.report.api.req.ByCarReportReq;
import com.htstar.ovms.report.api.req.CarReportReq;
import com.htstar.ovms.report.api.req.CostMonthReq;
import com.htstar.ovms.report.api.vo.CarReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费用报表数据
 *
 * @author lw
 * @date 2020-07-27 15:19:34
 */
@Mapper
public interface ReportExpenseMapper extends BaseMapper<ReportExpense> {

    /**
     *
     * 按月份统计费用总计费用
     * @param req
     * @return
     */
    ReportExpense getTotalCostByMonth(@Param("req") CostMonthReq req);

    /**
     * 按月统计列表
     * @param req1
     * @return
     */
    List<ReportExpense> getCostTableByMonth(@Param("req") CostMonthReq req1);

    /**
     * 按车辆统计 每月总费用
     * @param etpId
     * @param monthShort
     * @return
     */
    ReportExpense getTotalCostByCar(@Param("etpId")  Integer etpId, @Param("monthShort") String monthShort);

    /**
     * 按车辆统计费用报表
     * @param req
     * @return
     */
    IPage<ReportExpense> getCostPageByCar(@Param("req")  CarReportReq req);

    /**
     * Description:按车辆统计用车
     * Author: flr
     * Date: 2020/8/4 11:57
     * Company: 航通星空
     * Modified By:
     */
    List<CarReportVO> getUseCarReport(ByCarReportReq req);
}
