package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.entity.CarInsItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarInsItemPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 公车保险表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Mapper
public interface CarInsItemMapper extends BaseMapper<CarInsItem> {
    /**
     *保险信息分页
     * @param page
     * @param carItemPageReq
     * @return
     */
    @SqlParser(filter = true)
    IPage<CarInsItemPageVO> queryPage(@Param("query") CarItemPageReq carItemPageReq);

    /**
     * 企业车辆
     * @param etpId
     * @return
     */
    List<Integer> getIdList(Integer etpId);
    /**
     * 导出
     * @param carIdList
     * @return
     */
    List<CarInsItemPageVO> exportExcel(@Param("query")ExportReq req);

    /**
     * 企业保险过期数
     * @param etpId
     * @return
     */
    Integer expiredNumByEtp(@Param("etpId") Integer etpId,@Param("date") LocalDate date);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    CarInsItemPageVO getItemById(Integer id);

    /**
     * 获取用户保存的信息
     * @param userId
     * @return
     */
    CarInsItemPageVO getItemByUser(Integer userId);
}
