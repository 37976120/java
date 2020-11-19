package com.htstar.ovms.enterprise.service;

import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.vo.ApplyCarDataVo;
import com.htstar.ovms.enterprise.api.vo.CarDataVo;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/16
 * Company: 航通星空
 * Modified By:
 */
public interface CarDataAnalysisService {

    /**
     * 获取企业车辆数据概要
     * @return
     */
    public CarDataVo getCarDataByEtp();

    /**
     * 用车数据总览
     * @param timeType
     * @return
     */
    public ApplyCarDataVo getApplyCarData(Integer timeType);

    /**
     * 监控数据统计
     * @param timeType
     * @return
     */
    public R getAlarmData(Integer timeType);
}
