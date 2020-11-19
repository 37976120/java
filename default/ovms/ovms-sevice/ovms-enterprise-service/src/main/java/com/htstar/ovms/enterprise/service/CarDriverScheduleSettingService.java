package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.enterprise.api.dto.CarDriverScheduleDTO;
import com.htstar.ovms.enterprise.api.dto.CarDriverScheduleNoPageDTO;
import com.htstar.ovms.enterprise.api.entity.CarDriverScheduleSetting;
import com.htstar.ovms.enterprise.api.vo.ApplyCarOrderAndDriverVO;
import com.htstar.ovms.enterprise.api.vo.CarDriverScheduleSettingVO;

import java.util.List;

/**
 * 设置车辆或者司机排班规则
 *
 * @author HanGuJi
 * @date 2020-07-01 15:03:18
 */
public interface CarDriverScheduleSettingService extends IService<CarDriverScheduleSetting> {

    /**
     * 保存车辆或者司机排班规则
     * @param carDriverScheduleSetting
     * @return
     */
    boolean saveScheduleSetting(CarDriverScheduleSetting carDriverScheduleSetting);

    /**
     * 根据车辆或司机id查询排班设置信息以及车辆或司机相关信息
     * @param carAndDriverId
     * @return
     */
    CarDriverScheduleSettingVO getCarOrDriverScheduleSetting(Integer carAndDriverId, Integer flag);

    /**
     * 可以分配的车辆和司机列表
     * @param carDriverScheduleDTO
     * @return
     */
    Page<ApplyCarOrderAndDriverVO> getAbleAllocationCarAndDriver(CarDriverScheduleDTO carDriverScheduleDTO);

    /**
     * 车辆或者司机排班时间列表
     * @param carDriverScheduleDTO
     * @return
     */
    Page<ApplyCarOrderAndDriverVO> getCarDriverSchedulePage(CarDriverScheduleDTO carDriverScheduleDTO);
    /**
     * 车辆或者司机排班时间列表总数
     * @param carDriverScheduleDTO
     * @return
     */
    int carInfoDevicetotal(CarDriverScheduleNoPageDTO carDriverScheduleDTO);
    /**
     * 获取正在使用的车辆id
     */
    List<Integer> getUsingCarIds();
}
