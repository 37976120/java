package com.htstar.ovms.enterprise.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.CarAlarmRemind;
import com.htstar.ovms.enterprise.api.entity.CarDevice;

/**
 * 车-设备 中间表
 *
 * @author lw
 * @date 2020-06-28 16:48:51
 */
public interface CarDeviceService extends IService<CarDevice> {
    /** 新增
     * @param carId
     * @param deviceSn
     * @return
     */
    R save(Integer carId,String deviceSn);

    /**
     * 修改
     * @param carId
     * @param deviceSn
     * @return
     */
    R update(Integer carId,String deviceSn);

    /**
     * 检查设备是否可以绑定
     * @param deviceSn
     * @return
     */
    R checkDeviceIsBinding(String deviceSn);

    /**
     * 根据车辆id获取设备号
     * @param carId
     * @return
     */
    String getDeviceSnByCarId(Integer carId);

    /**
     * 获取企业已经绑定了设备的车辆
     * @param etpId
     * @return
     */
    Integer  getDeviceSnByEtp(Integer etpId);

    /**
     * 获取设备在线数
     * @param etpId
     * @return
     */
    Integer getOnline(Integer etpId);

    /**
     * 设置安防提醒信息
     * @param carAlarmRemind
     * @return
     */
    R setAlarmRemind(CarAlarmRemind carAlarmRemind);

    R getAlarmRemind(Integer carId);

}
