package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.dto.CarSchedulingTimeDTO;
import com.htstar.ovms.enterprise.api.dto.CarSchedulingTimeWhereDTO;
import com.htstar.ovms.enterprise.api.entity.CarSchedulingTime;
import com.htstar.ovms.enterprise.api.vo.CarSchedulingTimeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 12:07:04
 */
@Mapper
public interface CarSchedulingTimeMapper extends BaseMapper<CarSchedulingTime> {
    /**
     * 查询排班日期
     * @param licCode
     * @return
     */
     Page<CarSchedulingTimeVO> getSchedulingAll( CarSchedulingTimeDTO carSchedulingTimeDTO);
    /**
     * 通过车牌号查询
     * @param licCode
     * @return
     */
    int getBylicCode(@Param("sche") CarSchedulingTimeWhereDTO carSchedulingTimeWhereDTO);
    /**
     * 更具日期时间判断是否可以使用车，排班新车
     * @return
     */
    int getBylicCodeCount(@Param("licCode") String licCode);
}
