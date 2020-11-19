package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.entity.CarAccidItem;
import com.htstar.ovms.enterprise.api.req.CarItemExportReq;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.req.ExportReq;
import com.htstar.ovms.enterprise.api.vo.CarAccidItemPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆事故信息
 *
 * @author lw
 * @date 2020-06-22 10:11:23
 */
@Mapper
public interface CarAccidItemMapper extends BaseMapper<CarAccidItem> {

    /**
     * 分页查询
     * @param carItemPageReq
     * @return
     */
    IPage<CarAccidItemPageVO> queryPage(@Param("query") CarItemPageReq carItemPageReq);

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
    List<CarAccidItemPageVO> exportExcel(@Param("query") ExportReq req);


    CarAccidItemPageVO getItemById(Integer id);

    CarAccidItemPageVO getItemByUser(Integer userId);
}
