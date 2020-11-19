package com.htstar.ovms.device.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.device.api.dto.*;
import com.htstar.ovms.device.api.entity.CarViolations;
import com.htstar.ovms.device.api.vo.CarViolationsVO;
import com.htstar.ovms.device.api.vo.DeviceTripCountTotalVO;
import com.htstar.ovms.device.api.vo.DeviceTripVO;
import com.htstar.ovms.device.mapper.CarViolationsMapper;
import com.htstar.ovms.device.service.CarViolationsService;
import com.htstar.ovms.device.service.DeviceTripService;
import com.htstar.ovms.device.util.Cp;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 18:39:11
 */
@Service
public class CarViolationsServiceImpl extends ServiceImpl<CarViolationsMapper, CarViolations> implements CarViolationsService {
   @Autowired
   DeviceTripService deviceTripService;

    @Override
    public Page<CarViolationsVO> getAllCarViolationsPage(CarViolationsDTO carViolationsDTO) {
        DeviceTripDTO cp= this.getEdpId(carViolationsDTO);
        OvmsUser user = SecurityUtils.getUser();
        DeviceTripListDTO cp2 = Cp.cp(cp, new DeviceTripListDTO());
        CarViolationsListDTO cp1 = Cp.cp(carViolationsDTO, new CarViolationsListDTO());
        List<CarViolationsVO> list = new ArrayList<>();
        carViolationsDTO.setEtpId(user.getEtpId());
        for (CarViolationsVO carViolationsVO : baseMapper.getAllCarViolationsPage(carViolationsDTO).getRecords()) {
                cp.setLicCode(carViolationsVO.getLicCode());;
                cp.setStartTime(carViolationsVO.getStaTime());
                cp.setEndTime(carViolationsVO.getEndTime());
            DeviceTripTotalDTO deviceTripTotalDTO =  new DeviceTripTotalDTO();
            BeanUtils.copyProperties(cp,deviceTripTotalDTO);
            cp2.setLicCode(carViolationsVO.getLicCode());
            DeviceTripCountTotalVO tripInfoByDeviceSnPage = deviceTripService.getTripInfoByDeviceSn(cp2);
            if(tripInfoByDeviceSnPage != null){
                if(tripInfoByDeviceSnPage.getDeviceTrips() != null){
                for (DeviceTripVO deviceTripVO : tripInfoByDeviceSnPage.getDeviceTrips()) {
                    carViolationsVO.setStaAddr(deviceTripVO.getStaAddr());
                    carViolationsVO.setEndAddr(deviceTripVO.getEndAddr());
                    carViolationsVO.setStaLatlon(deviceTripVO.getStaLatlon());
                    carViolationsVO.setEndLatlon(deviceTripVO.getEndLatlon());
                }
            }
            }else {
                carViolationsVO.setStaAddr(null);
                carViolationsVO.setEndAddr(null);
                carViolationsVO.setStaLatlon(null);
                carViolationsVO.setEndLatlon(null);
            }
            list.add(carViolationsVO);
        }

        Page<CarViolationsVO> pages = new Page<>();
        pages.setRecords(list);
        pages.setTotal(baseMapper.getAllCarViolationsCount(cp1));
        return pages;
    }
    
public DeviceTripDTO getEdpId(CarViolationsDTO carViolationsDTO){
    OvmsUser user = SecurityUtils.getUser();
    DeviceTripDTO cp = Cp.cp(carViolationsDTO, new DeviceTripDTO());
    if (carViolationsDTO.getEtpId() == null && user != null) {//pc端企业判断
        if (user.getEtpId() != CommonConstants.ETP_ID_1) {
            carViolationsDTO.setEtpId(user.getEtpId());
        }else{
            carViolationsDTO.setEtpId(null);
        }
    }
    return cp;
}

    @Override
    public List<CarViolationsVO> exportCarViolationsVO(CarViolationsDTO carViolationsDTO) {
        OvmsUser user = SecurityUtils.getUser();
        CarViolationsListDTO cp1 = Cp.cp(carViolationsDTO, new CarViolationsListDTO());
        if(carViolationsDTO.getId() != null ){
            int[] objects = Arrays.stream(carViolationsDTO.getId().split(","))
                    .mapToInt(s-> Integer.parseInt(s)).toArray();
            cp1.setId(objects);
        }
        DeviceTripDTO cp = Cp.cp(cp1, new DeviceTripDTO());
        if (carViolationsDTO.getEtpId() == null && user != null) {//pc端企业判断
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                cp1.setEtpId(user.getEtpId());
            }else{
                cp1.setEtpId(null);
            }
        }
        List<CarViolationsVO> list = new ArrayList<>();

        baseMapper.getAllCarViolations(cp1).forEach(carViolationsVO -> {
            cp.setLicCode(carViolationsVO.getLicCode());
            cp.setStartTime(carViolationsVO.getStaTime());
            cp.setEndTime(carViolationsVO.getEndTime());
            DeviceTripCountTotalVO tripInfoByDeviceSnPage = deviceTripService.getTripInfoByDeviceSnPage(cp);
            if(tripInfoByDeviceSnPage != null){
                tripInfoByDeviceSnPage.getDeviceTrips().forEach(deviceTripVO -> {
                    carViolationsVO.setStaAddr(deviceTripVO.getStaAddr());
                    carViolationsVO.setEndAddr(deviceTripVO.getEndAddr());
                    carViolationsVO.setStaLatlon(deviceTripVO.getStaLatlon());
                    carViolationsVO.setEndLatlon(deviceTripVO.getEndLatlon());
                });
            }else {
                carViolationsVO.setStaAddr(null);
                carViolationsVO.setEndAddr(null);
                carViolationsVO.setStaLatlon(null);
                carViolationsVO.setEndLatlon(null);
            }
            list.add(carViolationsVO);
        });
        return list;
    }
}
