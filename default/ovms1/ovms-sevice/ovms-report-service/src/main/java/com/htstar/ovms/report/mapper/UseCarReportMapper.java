package com.htstar.ovms.report.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.report.api.req.ByCarReportPageReq;
import com.htstar.ovms.report.api.req.ByCarReportReq;
import com.htstar.ovms.report.api.req.MyPage;
import com.htstar.ovms.report.api.req.PersonalReportReq;
import com.htstar.ovms.report.api.vo.CarReportVO;
import com.htstar.ovms.report.api.vo.CarUseVO;
import com.htstar.ovms.report.api.vo.CountVO;
import com.htstar.ovms.report.api.vo.PersonalReportReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/29
 * Company: 航通星空
 * Modified By:
 */
@Mapper
public interface UseCarReportMapper {

    List<Integer> getCarIdList(@Param("licCode") String licCode, @Param("etpId") int etpId);

    List<CountVO> queryMonthUseCarCount(Map<String, Object> params);

    CarUseVO getCarUseVO(Map<String, Object> params);

    IPage<CarReportVO> getUseCarPage(ByCarReportPageReq req);

    Map<String, Object> getUseCarSum(ByCarReportReq req);

    List<CarReportVO> getUseCarList(ByCarReportReq byCarReportReq);

    CarUseVO getTotalCarUseVO(Map<String, Object> totalParams);

    MyPage<PersonalReportReqVO> personalReport(@Param("req") PersonalReportReq req);

    List<PersonalReportReqVO> personalReportNopage(@Param("req") PersonalReportReq req);
}
