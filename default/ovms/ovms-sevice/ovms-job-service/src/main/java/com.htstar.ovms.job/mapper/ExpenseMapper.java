package com.htstar.ovms.job.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * Description:
 * @Author: lw
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:
 */
@Mapper
public interface ExpenseMapper extends BaseMapper<T> {

    /**
     * 获取所有有效车牌
     * @return
     */
    List<Integer> getCarIds();

    /**
     * 保存油费
     * @param list
     * @param startTime
     * @param endTime
     */
    void saveFuelCost(@Param("list") List<Integer> list, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);

    /**
     * 获取etc费用
     * @param list
     * @param startTime
     * @param endTime
     */
    void saveEtcCost(@Param("list") List<Integer> list, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);

    /**
     * 保险费用
     * @param list
     * @param startTime
     * @param endTime
     */
    void saveInsCost(@Param("list") List<Integer> list, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);

    /**
     * 保养费用
     * @param list
     * @param startTime
     * @param endTime
     */
    void saveMaiCost(@Param("list") List<Integer> list, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);

    /**
     * 年检费用
     * @param list
     * @param startTime
     * @param endTime
     */
    void saveMotCost(@Param("list") List<Integer> list, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);

    /**
     * 维修费用
     * @param list
     * @param startTime
     * @param endTime
     */
    void saveRepairCost(@Param("list") List<Integer> list, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);

    /**
     * 停车费
     * @param list
     * @param startTime
     * @param endTime
     */
    void saveStopCost(@Param("list") List<Integer> list, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);

    /**
     * 洗车费
     * @param list
     * @param startTime
     * @param endTime
     */
    void saveWashCost(@Param("list") List<Integer> list, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);

    /**
     * 罚单费用
     * @param list
     * @param startTime
     * @param endTime
     */
    void saveTicketCost(@Param("list") List<Integer> list, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);

    /**
     * 汽车用品费
     * @param list
     * @param startTime
     * @param endTime
     */
    void saveSuppliesCost(@Param("list") List<Integer> list, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);

    /**
     * 其他费用
     * @param list
     * @param startTime
     * @param endTime
     */
    void saveOtherCost(@Param("list") List<Integer> list, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);
}
