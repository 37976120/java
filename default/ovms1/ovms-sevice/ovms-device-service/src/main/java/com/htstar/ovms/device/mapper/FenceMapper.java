package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.device.api.dto.FenceDTO;
import com.htstar.ovms.device.api.entity.Fence;
import com.htstar.ovms.device.api.vo.CarFenceRelationVO;
import com.htstar.ovms.device.api.vo.FencePageVO;
import com.htstar.ovms.device.api.vo.FenceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 围栏
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
@Mapper
public interface FenceMapper extends BaseMapper<Fence> {

    List<FenceVO> queryFenceList(@Param("deviceSn") String deviceSn);

    /**
     * 分页查询围栏列表
     * @param fenceDTO
     * @return
     */
    Page<FencePageVO> getFencePage(@Param("query") FenceDTO fenceDTO);

    /**
     * 查询未被车辆绑定的围栏信息
     * @param fenceDTO
     * @return
     */
    Page<FencePageVO> getNotAddFenceByCarInfoPage(@Param("query") FenceDTO fenceDTO);
}
