package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.entity.CarMaiItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarMaiItemPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 公车保养表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Mapper
public interface CarMaiItemMapper extends BaseMapper<CarMaiItem> {
    @SqlParser(filter = true)
    IPage<CarMaiItemPageVO> queryPage(@Param("query") CarItemPageReq carItemPageReq);

    /**
     * 企业所有车辆数据
     * @param etpId
     * @return
     */
    List<Integer> getIdList(Integer etpId);

    /**
     * 导出
     * @param list
     * @return
     */
    List<CarMaiItemPageVO> exportExcel(@Param("query") ExportReq req);

    /**
     * 车辆保养过期数
     * @param etpId
     * @return
     */
    Integer expiredNumByEtp(@Param("etpId") Integer etpId,@Param("date") LocalDate date);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    CarMaiItemPageVO getItemById(Integer id);

    /**
     * 获取用户保存的
     * @param userId
     * @return
     */
    CarMaiItemPageVO getItemByUser(Integer userId);

}
