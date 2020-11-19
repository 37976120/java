package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.device.api.dto.FenceDTO;
import com.htstar.ovms.device.api.entity.Fence;
import com.htstar.ovms.device.api.vo.CarFenceRelationVO;
import com.htstar.ovms.device.api.vo.FencePageVO;
import com.htstar.ovms.device.api.vo.FenceVO;

import java.util.List;

/**
 * 围栏
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
public interface FenceService extends IService<Fence> {

    /**
     * 根据设备序列号查找围栏
     * @param deviceSn
     * @return
     */
    List<FenceVO> queryFenceList(String deviceSn);

    /**
     * 新增围栏
     * @param fence
     * @return
     */
    R saveFence(Fence fence);

    /**
     * 修改围栏
     * @param fence
     * @return
     */
    R updateFence(Fence fence);

    /**
     *
     * @param fenceDTO
     * @return
     */
    Page<FencePageVO> getFencePage(FenceDTO fenceDTO);

    /**
     * 车辆添加围栏信息
     * @param fenceDTO
     * @return
     */
    Page<FencePageVO> getNotAddFenceByCarInfoPage(FenceDTO fenceDTO);

    /**
     * 删除围栏，判断是否有绑定关系
     * @param id
     * @return
     */
    R removeFence(Integer id,boolean exit);
}
