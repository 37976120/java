package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.entity.CarMotItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarMotItemPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 公车年检表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Mapper
public interface CarMotItemMapper extends BaseMapper<CarMotItem> {
    /**
     *年检信息分页
     * @param page
     * @param carItemPageReq
     * @return
     */
    @SqlParser(filter = true)
    IPage<CarMotItemPageVO> queryPage(@Param("query") CarItemPageReq carItemPageReq);




    /**
     * 企业所有车辆
     * @param etpId
     * @return
     */
    List<Integer> getIdList(Integer etpId);

    /**
     * 导出
     * @param
     * @return
     */
    List<CarMotItemPageVO> exportExcel(@Param("query") ExportReq req);

    /**
     * 车辆年检过期数
     * @param etpId
     * @return
     */
    Integer expiredNumByEtp(@Param("etpId") Integer etpId,@Param("date") LocalDate date);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    CarMotItemPageVO getItemById(Integer id);
    /**
     * 获取用户保存的信息
     * @param userId
     * @return
     */
    CarMotItemPageVO getItemByUser(Integer userId);
}
