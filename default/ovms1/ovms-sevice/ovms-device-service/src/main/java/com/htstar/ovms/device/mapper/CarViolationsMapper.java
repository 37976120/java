package com.htstar.ovms.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.device.api.dto.CarViolationsDTO;
import com.htstar.ovms.device.api.dto.CarViolationsListDTO;
import com.htstar.ovms.device.api.entity.CarViolations;
import com.htstar.ovms.device.api.vo.CarViolationsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 18:39:11
 */
@Mapper
public interface CarViolationsMapper extends BaseMapper<CarViolations> {

    Page<CarViolationsVO> getAllCarViolationsPage(@Param("car") CarViolationsDTO carViolationsDTO);
    int getAllCarViolationsCount(@Param("car") CarViolationsListDTO carViolationsDTO);
    List<CarViolationsVO> getAllCarViolations(@Param("car") CarViolationsListDTO carViolationsDTO);
}
