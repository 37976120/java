package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.device.api.feign.DeviceAlarmFeign;
import com.htstar.ovms.enterprise.api.constant.TimeTypeConstant;
import com.htstar.ovms.enterprise.api.vo.ApplyCarDataVo;
import com.htstar.ovms.enterprise.api.vo.CarDataVo;
import com.htstar.ovms.enterprise.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/16
 * Company: 航通星空
 * Modified By:
 * @author liuwei
 */
@Service
@Slf4j
public class CarDataAnalysisServiceImpl implements CarDataAnalysisService {
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private CarDeviceService carDeviceService;
    @Autowired
    private CarInsItemService carInsItemService;
    @Autowired
    private ApplyCarOrderService carOrderService;
    @Autowired
    private DeviceAlarmFeign alarmFeign;
    @Autowired
    private CarMaiItemService carMaiItemService;
    @Autowired
    private CarMotItemService carMotItemService;

    /**
     * 获取企业车辆数据概要
     * @return
     */
    @Override
    public CarDataVo getCarDataByEtp() {
        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();
        //车辆数据
        CarDataVo carDataVo = new CarDataVo();
        //企业车辆数据 --已添加车辆数
        Integer carCount = carInfoService.getCarCount(etpId);
        //企业已绑定设备数量
        Integer deviceSnCount = carDeviceService.getDeviceSnByEtp(etpId);

        //设备在线数
        Integer online = carDeviceService.getOnline(etpId);
        LocalDate localDate=LocalDate.now();
        //保险过期
        Integer InsCount = carInsItemService.expiredNumByEtp(etpId, localDate);
        //保养过期
        Integer maiCount = carMaiItemService.expiredNumByEtp(etpId, localDate);
        //年检过期
        Integer motCount = carMotItemService.expiredNumByEtp(etpId, localDate);
        carDataVo.setCarCount(carCount);
        carDataVo.setDeviceSn(deviceSnCount);
        carDataVo.setInsurance(InsCount);
        carDataVo.setMaintenance(maiCount);
        carDataVo.setMot(motCount);
        carDataVo.setOnline(online);
        carDataVo.setNotOnline(deviceSnCount-online);
        carDataVo.setNoDeviceSn(carCount-deviceSnCount);
        return carDataVo;
    }

    /**
     * 用车数据
     * @param timeType
     * @return
     */
    @Override
    public ApplyCarDataVo getApplyCarData(Integer timeType) {
        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();
        Date date = this.getDate(timeType);
        ApplyCarDataVo applyCarDataVo = carOrderService.applyCarData(date, etpId);
        return applyCarDataVo;
    }

    /**
     * 企业警情数据
     * @param timeType
     * @return
     */
    @Override
    public R getAlarmData(Integer timeType){
        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();
        Date date = this.getDate(timeType);
        String s = DateUtil.formatDateTime(date);
        return alarmFeign.getAlarmData(etpId,s);
    }


    private Date getDate(Integer timeType){
        //当天的开始时间
       Date date= DateUtil.beginOfDay(DateUtil.date());
        if (timeType== TimeTypeConstant.SEVEN_DAY){
            date= DateUtil.offsetDay(date,  -7);
        }if (timeType==TimeTypeConstant.FIFTEEN_DAY){
            date=DateUtil.offsetDay(date,-15 );
        }
        if (timeType == TimeTypeConstant.THIRTY_DAY) {
            date = DateUtil.offsetDay(date, -30);
        }
        if (timeType == TimeTypeConstant.THREE_MONTHS) {
            date = DateUtil.offsetMonth(date, -3);
        }
        if (timeType == TimeTypeConstant.SIX_MONTHS){
            date=DateUtil.offsetMonth(date, -6);
        }
        if (timeType==TimeTypeConstant.ONE_YEAR){
            date= DateUtil.offset(date, DateField.YEAR, -1);
        }
        return date;
    }

}
