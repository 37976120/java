package com.htstar.ovms.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.device.api.dto.FenceDTO;
import com.htstar.ovms.device.api.entity.CarFenceRelation;
import com.htstar.ovms.device.api.vo.CarFenceRelationVO;
import com.htstar.ovms.device.api.vo.FencePageVO;
import com.htstar.ovms.device.mapper.CarFenceRelationMapper;
import com.htstar.ovms.device.service.CarFenceRelationService;
import com.htstar.ovms.device.service.GpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 车辆围栏关系
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
@Service
public class CarFenceRelationServiceImpl extends ServiceImpl<CarFenceRelationMapper, CarFenceRelation> implements CarFenceRelationService {

    @Autowired
    private GpsService gpsService;

    @Override
    public Page<CarFenceRelationVO> getCarFenceRelationPage(FenceDTO fenceDTO) {
        return baseMapper.getCarFenceRelationPage(fenceDTO);
    }

    @Override
    public Page<CarFenceRelationVO> getAddCarFenCenInfoPage(FenceDTO fenceDTO) {
        fenceDTO.setEtpId(TenantContextHolder.getEtpId());
        return baseMapper.getAddCarFenCenInfoPage(fenceDTO);
    }

    @Override
    public Page<CarFenceRelationVO> fenceRelationCarInfoPage(FenceDTO fenceDTO) {
        fenceDTO.setEtpId(TenantContextHolder.getEtpId());
        return baseMapper.fenceRelationCarInfoPage(fenceDTO);
    }

    @Override
    public Page<CarFenceRelationVO> selectFenceByCarIdPage(FenceDTO fenceDTO) {
        Page<CarFenceRelationVO> page = baseMapper.selectFenceByCarIdPage(fenceDTO);
        for (CarFenceRelationVO record : page.getRecords()) {
            if(record.getLat()!=null && record.getLng()!=null){
                record.setCenterAddr(gpsService.getGpsAddr(record.getLat(),record.getLng()));
            }
        }
        return page;
    }

    @Override
    public boolean getExits(Integer id) {
        Integer count = baseMapper.selectCount(new QueryWrapper<CarFenceRelation>().eq("fence_id", id));
        return count!=0?true:false;
    }


}
