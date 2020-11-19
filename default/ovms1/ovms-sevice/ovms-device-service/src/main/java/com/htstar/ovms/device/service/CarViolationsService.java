package com.htstar.ovms.device.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import com.htstar.ovms.device.api.dto.CarViolationsDTO;
import com.htstar.ovms.device.api.entity.CarViolations;
import com.htstar.ovms.device.api.vo.CarViolationsVO;

import java.util.List;

/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 18:39:11
 */
public interface CarViolationsService extends IService<CarViolations> {


    Page<CarViolationsVO> getAllCarViolationsPage(CarViolationsDTO carViolationsDTO);

    List<CarViolationsVO> exportCarViolationsVO(CarViolationsDTO carViolationsDTO);
}
