package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.enterprise.api.entity.CarFuelCard;
import com.htstar.ovms.enterprise.api.req.CarFileManageReq;
import com.htstar.ovms.enterprise.api.vo.FuelCardPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 油卡
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
@Mapper
public interface CarFuelCardMapper extends BaseMapper<CarFuelCard> {

    /**
     * 油卡分页
     * @param carFileManageReq
     * @return
     */
    IPage<FuelCardPageVO> queryPage(@Param("carFileManageReq") CarFileManageReq carFileManageReq);

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
    List<FuelCardPageVO> exportExcel(@Param("req") CarFileManageReq req);



}
