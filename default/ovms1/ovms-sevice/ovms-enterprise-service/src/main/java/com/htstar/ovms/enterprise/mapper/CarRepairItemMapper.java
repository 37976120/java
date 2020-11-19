package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.entity.CarRepairItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarRepairItemPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公车维修表

 *
 * @author lw
 * @date 2020-06-08 13:48:46
 */
@Mapper
public interface CarRepairItemMapper extends BaseMapper<CarRepairItem> {
    /**
     *维修信息分页
     * @param page
     * @param carItemPageReq
     * @return
     */
    @SqlParser(filter = true)
    IPage<CarRepairItemPageVO> queryPage(@Param("query") CarItemPageReq carItemPageReq);


    /**
     * 所有加油数据
     * @param etpId
     * @return
     */
    List<Integer> getIdList(Integer etpId);

    /**
     * 导出
     * @param
     * @return
     */
    List<CarRepairItemPageVO> exportExcel(@Param("query")  ExportReq req);

    CarRepairItemPageVO getItemById(Integer id);

    /**
     * @param userId
     * @return
     */
    CarRepairItemPageVO getItemByUser(Integer userId);
}
