package com.htstar.ovms.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.enterprise.api.entity.ApplyCarOrder;
import com.htstar.ovms.enterprise.api.entity.CarDriverScheduleSetting;
import com.htstar.ovms.enterprise.api.vo.ApplyCarOrderDerverVO;
import com.htstar.ovms.enterprise.api.vo.CarDriverScheduleSettingVO;
import com.htstar.ovms.enterprise.api.vo.CarInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设置车辆或者司机排班规则
 *
 * @author HanGuJi
 * @date 2020-07-01 15:03:18
 */
@Mapper
public interface CarDriverScheduleSettingMapper extends BaseMapper<CarDriverScheduleSetting> {

    /**
     * 根据车辆id查询车辆信息
     * @param carAndDriverId
     * @return
     */
    CarDriverScheduleSettingVO getCarInfo(@Param("carAndDriverId") Integer carAndDriverId);

    /**
     * 根据排班时间和是否排班查询列表
     * @param queryTime
     * @param carId
     * @param driverId
     * @return
     */
    List<ApplyCarOrderDerverVO> selectScheduleList(@Param("queryTime") String queryTime, @Param("carId") Integer carId, @Param("driverId")Integer driverId);


    /**
     * 获取正在使用的车辆id
     * @param etpId
     * @return
     */
    List<Integer> getUsingCarIds(Integer etpId);

    List<Integer> getUsingDrivers(Integer etpId);

    List<CarInfoVo> getSeetingLicCode(@Param("settingId") Integer settingId);
}
