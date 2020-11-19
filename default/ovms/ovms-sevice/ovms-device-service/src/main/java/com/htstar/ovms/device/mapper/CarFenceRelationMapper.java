package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.device.api.dto.FenceDTO;
import com.htstar.ovms.device.api.entity.CarFenceRelation;
import com.htstar.ovms.device.api.vo.CarFenceRelationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 车辆围栏关系
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
@Mapper
public interface CarFenceRelationMapper extends BaseMapper<CarFenceRelation> {

    /**
     * 根据围栏id查询绑定车辆信息
     */
    Page<CarFenceRelationVO> getCarFenceRelationPage(@Param("query")FenceDTO fenceDTO);

    /**
     * 查询没有绑定车辆信息列表
     * @param fenceDTO
     * @return
     */
    Page<CarFenceRelationVO> getAddCarFenCenInfoPage(@Param("query") FenceDTO fenceDTO);

    /**
     * 按车辆查询绑定围栏数
     * @param fenceDTO
     * @return
     */
    Page<CarFenceRelationVO> fenceRelationCarInfoPage(@Param("query")FenceDTO fenceDTO);

    /**
     * 根据车辆id查询绑定围栏信息
     * @param fenceDTO
     * @return
     */
    Page<CarFenceRelationVO> selectFenceByCarIdPage(@Param("query") FenceDTO fenceDTO);
}
