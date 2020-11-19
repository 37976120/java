package com.htstar.ovms.enterprise.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.dto.CarSchedulingTimeDTO;
import com.htstar.ovms.enterprise.api.dto.CarSchedulingTimeWhereDTO;
import com.htstar.ovms.enterprise.api.entity.CarSchedulingTime;
import com.htstar.ovms.enterprise.api.vo.CarSchedulingTimeVO;
import com.htstar.ovms.enterprise.mapper.CarSchedulingTimeMapper;
import com.htstar.ovms.enterprise.service.CarSchedulingTimeService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 12:07:04
 */
@Service
public class CarSchedulingTimeServiceImpl extends ServiceImpl<CarSchedulingTimeMapper, CarSchedulingTime> implements CarSchedulingTimeService {


    /**
     * 查询排班日期
     * @param
     * @return
     */
    @Override
    public Page<CarSchedulingTimeVO> getSchedulingAll(CarSchedulingTimeDTO carSchedulingTimeDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if ( user != null) {
                carSchedulingTimeDTO.setEtpId(user.getEtpId());
        }

        Page<CarSchedulingTimeVO> schedulingAll = baseMapper.getSchedulingAll(carSchedulingTimeDTO);
        List<CarSchedulingTimeVO> list = new ArrayList<>();
        if(!schedulingAll.getRecords().isEmpty()){
            schedulingAll.getRecords().forEach(carSchedulingTimeVO -> {
                int[] a = Arrays.stream(carSchedulingTimeVO.getCarId().split(",")).mapToInt(s -> Integer.parseInt(s)).toArray();
                String[] strArr = carSchedulingTimeVO.getLicCodes().split(",");//注意分隔符是需要转译
                Map<String,Object> map = new HashMap<>();
                List<Map<String,Object>> maplist = new ArrayList<>();
                for (int i = 0; i < a.length; i++) {
                    map = new HashMap<>();
                    map.put("id",a[i]);
                    map.put("licCode",strArr[i]);
                    maplist.add(map);
                   
                }
                carSchedulingTimeVO.setLicCodesList(maplist);
                list.add(carSchedulingTimeVO);
            });

            schedulingAll.setRecords(list);
        }

        return schedulingAll;
    }
    /**
     * 通过车牌号查询
     * @param
     * @return
     */
    @Override
    public int getBylicCode(CarSchedulingTimeWhereDTO carSchedulingTimeWhereDTO) {

        return baseMapper.getBylicCode(carSchedulingTimeWhereDTO);
    }
    /**
     * 更具日期时间判断是否可以使用车，排班新车
     * @return
     */
    @Override
    public int getBylicCodeCount(String licCode) {
        return baseMapper.getBylicCodeCount(licCode);
    }
}
