package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.dto.CarSchedulingTimeDTO;
import com.htstar.ovms.enterprise.api.dto.CarSchedulingTimeWhereDTO;
import com.htstar.ovms.enterprise.api.entity.CarSchedulingTime;
import com.htstar.ovms.enterprise.api.vo.CarSchedulingTimeVO;

import java.util.List;


/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 12:07:04
 */
public interface CarSchedulingTimeService extends IService<CarSchedulingTime> {
    /**
     * 查询车辆排班
     * @return
     */
    Page<CarSchedulingTimeVO> getSchedulingAll(CarSchedulingTimeDTO carSchedulingTimeDTO);

    /**
     * 通过车牌号查询
     * @param
     * @return
     */
    int getBylicCode(CarSchedulingTimeWhereDTO carSchedulingTimeWhereDTO);
    /**
     * 更具日期时间判断是否可以使用车，排班新车
     * @return
     */
    int getBylicCodeCount(String licCode);

}
