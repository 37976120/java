package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.entity.CarFuelItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarFuelItemPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公车加油表
 *
 * @author lw
 * @date 2020-06-08 13:48:44
 */
@Mapper
public interface CarFuelItemMapper extends BaseMapper<CarFuelItem> {
    /**
     *加油信息分页
     * @param carItemPageReq
     * @return
     */
    @SqlParser(filter = true)
    IPage<CarFuelItemPageVO> queryPage(@Param("query") CarItemPageReq carItemPageReq);


    /**
     * 获取所有id
     * @param etpId
     * @return
     */
    List<Integer> getIdList(int etpId);


    /**
     * 导出
     * @param req
     * @return
     */
    List<CarFuelItemPageVO> exportExcel(@Param("query") ExportReq req);

    /**
     * 根据编号获取未存档消费金额
     * @param cardId
     * @return
     */
    Integer getSumCostByCard(Integer cardId);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    CarFuelItemPageVO getCarFuelItemById(Integer id);

    /**
     * 获取用户保存的信息
     * @param userId
     * @return
     */
    CarFuelItemPageVO getItemByUser(Integer userId);
}
