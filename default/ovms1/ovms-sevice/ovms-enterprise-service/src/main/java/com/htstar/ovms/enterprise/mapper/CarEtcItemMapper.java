package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.entity.CarEtcItem;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarEtcItemPageVO;
import com.htstar.ovms.report.api.entity.ReportDriverExpense;
import com.htstar.ovms.report.api.entity.ReportExpense;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * etc通行记录

 *
 * @author lw
 * @date 2020-06-22 10:11:23
 */
@Mapper
public interface CarEtcItemMapper extends BaseMapper<CarEtcItem> {

    /**
     * 分页
     * @param carItemPageReq
     * @return
     */
    @SqlParser(filter = true)
    IPage<CarEtcItemPageVO> queryPage(@Param("query") CarItemPageReq carItemPageReq);
    /**
     * 获取所有id
     * @param etpId
     * @return
     */
    List<Integer> getIdList(int etpId);

    /**
     * 导出
     * @param
     * @return
     */
    List<CarEtcItemPageVO> exportExcel(@Param("query") ExportReq req);

    /**
     * 根据卡id获取未存档消费金额
     * @param cardId
     * @return
     */
    Integer getSumCostByCard(Integer cardId);


    /**
     * 新增车辆费用报表
     * @param reportExpense
     * @return
     */
    void saveReportExpenseByCar(@Param("rep") ReportExpense reportExpense);

    /**
     * 保存司机费用报表数据
     * @param reportDriverExpense
     */
    void  saveReportExpenseByDriver(@Param("rep") ReportDriverExpense reportDriverExpense);


    /**
     * 根据id获取
     * @param id
     * @return
     */
    CarEtcItemPageVO getCarEtcItemById(Integer id);


    /**
     * 获取用户保存的信息 只有一条
     * @param userId
     * @return
     */
    CarEtcItemPageVO getCarEtcItemByUser(Integer userId);
}
