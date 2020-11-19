package com.htstar.ovms.report.mapper;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.report.api.req.ByDrivingReportPageReq;
import com.htstar.ovms.report.api.vo.CountVO;
import com.htstar.ovms.report.api.vo.TotalVO;
import com.htstar.ovms.report.api.vo.VceTotalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: JinZhu
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:  行驶报表
 */
@Mapper
public interface DrivingReportMapper {

    List<Integer> getDvgIdList(@Param("licCode") String licCode, @Param("etpId")int etpId);

    List<TotalVO> queryMonthDrivingCount(Map<String,Object> params);

    IPage<VceTotalVO> queryVceMonthDrivingCount(ByDrivingReportPageReq req);

    IPage<VceTotalVO> queryDrivingMonthDrivingCount(ByDrivingReportPageReq req);
}
