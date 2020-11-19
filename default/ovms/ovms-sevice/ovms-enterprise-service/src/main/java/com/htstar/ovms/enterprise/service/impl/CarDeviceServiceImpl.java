package com.htstar.ovms.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.device.api.feign.DeviceFeign;
import com.htstar.ovms.enterprise.api.entity.CarAlarmRemind;
import com.htstar.ovms.enterprise.api.entity.CarDevice;
import com.htstar.ovms.enterprise.mapper.CarDeviceMapper;
import com.htstar.ovms.enterprise.service.CarDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 车-设备 中间表
 *
 * @author lw
 * @date 2020-06-28 16:48:51
 */
@Service
@Slf4j
public class CarDeviceServiceImpl extends ServiceImpl<CarDeviceMapper, CarDevice> implements CarDeviceService {
    @Autowired
    private DeviceFeign deviceFeign;



    /**
     * 新增
     *
     * @param carId
     * @param deviceSn
     * @return
     */
    @Override
    public R save(Integer carId, String deviceSn) {
        CarDevice carDevice1 = baseMapper.selectOne(new QueryWrapper<CarDevice>().eq("car_id", carId));
        if (carDevice1!=null){
            return this.update(carId,deviceSn );
        }
        CarDevice carDevice = new CarDevice();
        carDevice.setCarId(carId);
        carDevice.setDeviceSn(deviceSn);
        this.save(carDevice);
        return R.ok("设备添加成功");

    }

    @Override
    public R update(Integer carId, String deviceSn) {
        CarDevice oldCarDevice = baseMapper.selectOne(new QueryWrapper<CarDevice>().eq("car_id", carId));
        if (oldCarDevice.getDeviceSn().equals(deviceSn)) {
            return R.ok("设备绑定成功");
        }
        //检查新设备是否可以绑定
        R r = this.checkDeviceIsBinding(deviceSn);
        if (r.getCode() == 1) {
            return r;
        }
        oldCarDevice.setDeviceSn(deviceSn);
        baseMapper.updateById(oldCarDevice);
        return R.ok("设备绑定成功");
    }

    /**
     * 检查设备是否可以绑定
     *
     * @param deviceSn
     * @return
     */
    @Override
    public R checkDeviceIsBinding(String deviceSn) {
        Integer count = baseMapper.selectCount(new QueryWrapper<CarDevice>().eq("device_sn", deviceSn));
        if (count > 0) {
            return R.failed("该设备已绑定其他车辆,添加失败");
        }
        R r = deviceFeign.checkBinding(deviceSn);
        if (r.getCode() == 1) {
            return r;
        }

        return R.ok();
    }

    /**
     * 根据车辆id 获取设备号
     *
     * @param carId
     * @return
     */
    @Override
    public String getDeviceSnByCarId(Integer carId) {
        CarDevice carDevice = baseMapper.selectOne(new QueryWrapper<CarDevice>().eq("car_id", carId));
        if (carDevice == null) {
            return "";
        }
        return carDevice.getDeviceSn();
    }

    /**
     * 企业已经绑定车辆的设备
     *
     * @param etpId
     * @return
     */
    @Override
    public Integer getDeviceSnByEtp(Integer etpId) {
        return baseMapper.getDeviceSnByEtp(etpId);

    }

    @Override
    public Integer getOnline(Integer etpId) {
        return baseMapper.getOnline(etpId);
    }

    @Override
    public R setAlarmRemind(CarAlarmRemind carAlarmRemind) {
        log.info("开关信息为{}",carAlarmRemind );
        Integer carId = carAlarmRemind.getCarId();
        if (carId == null) {
            return R.failed("请先保存车辆信息");
        }
        CarDevice carDevice = baseMapper.selectOne(new QueryWrapper<CarDevice>().eq("car_id", carId));
        if (carDevice == null) {
            return R.failed("请先给车辆绑定设备");
        }

        baseMapper.setAlarmRemind(carAlarmRemind);
        return R.ok();
    }

    /**
     * 获取警情开关
     * @param carId
     * @return
     */
    @Override
    public R getAlarmRemind(Integer carId) {
        return  R.ok(baseMapper.getAlarmRemind(carId));
    }

}
