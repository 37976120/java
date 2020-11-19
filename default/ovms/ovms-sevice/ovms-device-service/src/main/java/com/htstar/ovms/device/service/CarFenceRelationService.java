package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.device.api.dto.FenceDTO;
import com.htstar.ovms.device.api.entity.CarFenceRelation;
import com.htstar.ovms.device.api.vo.CarFenceRelationVO;

/**
 * 车辆围栏关系
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
public interface CarFenceRelationService extends IService<CarFenceRelation> {

    /**
     * 根据围栏id查询绑定车辆，或者添加绑定车辆列表
     * @param fenceDTO
     * @return
     */
    Page<CarFenceRelationVO> getCarFenceRelationPage(FenceDTO fenceDTO);

    /**
     *查询没有绑定车辆信息列表
     * @param fenceDTO
     * @return
     */
    Page<CarFenceRelationVO> getAddCarFenCenInfoPage(FenceDTO fenceDTO);

    /**
     *按车辆查询绑定围栏数
     * @param fenceDTO
     * @return
     */
    Page<CarFenceRelationVO> fenceRelationCarInfoPage(FenceDTO fenceDTO);

    /**
     * 根据车辆id查询绑定围栏信息
     * @param fenceDTO
     * @return
     */
    Page<CarFenceRelationVO> selectFenceByCarIdPage(FenceDTO fenceDTO);

    /**
     * 判断围栏是否有绑定关系
     * @param id
     * @return true 存在 false 不存在
     */
    boolean getExits(Integer id);
}
