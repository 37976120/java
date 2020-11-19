package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.entity.CarOtherItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarOtherItemPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公车其他项目表

 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Mapper
public interface CarOtherItemMapper extends BaseMapper<CarOtherItem> {
    /**
     *其他项目分页
     * @param carItemPageReq
     * @return
     */
    @SqlParser(filter = true)
    IPage<CarOtherItemPageVO> queryPage(@Param("query") CarItemPageReq carItemPageReq);

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
    List<CarOtherItemPageVO> exportExcel(@Param("query") ExportReq req);


    /**
     * 根据id获取
     * @param id
     * @return
     */
    CarOtherItemPageVO getItemById(int id);

    /**
     * 获取用户保存的
     * @param itemType
     * @param userId
     * @return
     */
    CarOtherItemPageVO getItemByUser(@Param("itemType") Integer itemType, @Param("userId") Integer userId);
}
