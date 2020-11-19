package com.htstar.ovms.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.report.api.entity.ReportDriverExpense;
import com.htstar.ovms.report.api.req.CarReportReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 司机费用

 *
 * @author lw
 * @date 2020-08-01 10:11:28
 */
@Mapper
public interface ReportDriverExpenseMapper extends BaseMapper<ReportDriverExpense> {

    /**
     * 获取当月总费用
     * @param monthShort
     * @param etpId
     * @return
     */
    ReportDriverExpense getTotalCostByDriver(@Param("monthShort") String monthShort,@Param("etpId") Integer etpId);

    /**
     * 司机分页
     * @param carReportReq
     * @return
     */
    IPage<ReportDriverExpense> getCostPageByDriver(@Param("req") CarReportReq carReportReq);
}
