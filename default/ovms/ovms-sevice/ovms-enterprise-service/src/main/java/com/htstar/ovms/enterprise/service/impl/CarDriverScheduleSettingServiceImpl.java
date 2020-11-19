package com.htstar.ovms.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.feign.RemoteUserService;
import com.htstar.ovms.admin.api.vo.UserVO;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.dto.CarDriverScheduleDTO;
import com.htstar.ovms.enterprise.api.dto.CarDriverScheduleNoPageDTO;
import com.htstar.ovms.enterprise.api.entity.CarDriverInfo;
import com.htstar.ovms.enterprise.api.entity.CarDriverScheduleSetting;
import com.htstar.ovms.enterprise.api.vo.ApplyCarOrderAndDriverVO;
import com.htstar.ovms.enterprise.api.vo.ApplyCarOrderDerverVO;
import com.htstar.ovms.enterprise.api.vo.CarDriverScheduleSettingVO;
import com.htstar.ovms.enterprise.util.Cp;
import com.htstar.ovms.enterprise.mapper.CarDriverScheduleSettingMapper;
import com.htstar.ovms.enterprise.service.CarDriverInfoService;
import com.htstar.ovms.enterprise.service.CarDriverScheduleSettingService;
import com.htstar.ovms.enterprise.service.CarInfoService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 设置车辆或者司机排班规则
 *
 * @author HanGuJi
 * @date 2020-07-01 15:03:18
 */
@Service
@Slf4j
public class CarDriverScheduleSettingServiceImpl extends ServiceImpl<CarDriverScheduleSettingMapper, CarDriverScheduleSetting> implements CarDriverScheduleSettingService {

    @Autowired
    private CarDriverInfoService carDriverInfoService;

    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private CarInfoService carInfoService;

    @Override
    public boolean saveScheduleSetting(CarDriverScheduleSetting carDriverScheduleSetting) {
        int result = 0;
        if (carDriverScheduleSetting.getId() != null) {
//            if(carDriverScheduleSetting.getEndTime() == null || carDriverScheduleSetting.getStartTime() == null){
//                carDriverScheduleSetting.setEndTime(null);
//                carDriverScheduleSetting.setStartTime(null);
//            }
            result = baseMapper.updateById(carDriverScheduleSetting);
        } else {
            result = baseMapper.insert(carDriverScheduleSetting);
        }
        return result == 1 ? true : false;
    }

    @Override
    public CarDriverScheduleSettingVO getCarOrDriverScheduleSetting(Integer carAndDriverId, Integer flag) {
        CarDriverScheduleSettingVO carDriverScheduleVO = new CarDriverScheduleSettingVO();
        CarDriverScheduleSetting carDriverScheduleSetting = baseMapper.selectOne(new QueryWrapper<CarDriverScheduleSetting>()
                .eq("car_id", carAndDriverId)
                .or()
                .eq("driver_id", carAndDriverId)
                .last("limit 1"));
        if (carDriverScheduleSetting != null) {
            if (flag == 1) {
                //获取车辆信息
                carDriverScheduleVO = baseMapper.getCarInfo(carAndDriverId);
            } else {
                //获取司机信息
                QueryWrapper<CarDriverInfo> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id", carAndDriverId).eq("del_flag", 0);
                CarDriverInfo driverInfo = carDriverInfoService.getOne(wrapper);
                if (driverInfo != null) {
                    carDriverScheduleVO.setLicenseType(driverInfo.getLicenseType());
                    carDriverScheduleVO.setDriverId(driverInfo.getId());
                    R<UserVO> userVO = remoteUserService.user(driverInfo.getUserId());
                    if (userVO != null && userVO.getData() != null) {
                        carDriverScheduleVO.setDriverName(userVO.getData().getNickName());
                        carDriverScheduleVO.setPhone(userVO.getData().getPhone());
                    }
                }
            }
            BeanUtils.copyProperties(carDriverScheduleSetting, carDriverScheduleVO);
        }
        return carDriverScheduleVO;
    }


    @Override
    public Page<ApplyCarOrderAndDriverVO> getAbleAllocationCarAndDriver(CarDriverScheduleDTO carDriverScheduleDTO) {
        String startTime = carDriverScheduleDTO.getStartTime();
        String endTime = carDriverScheduleDTO.getEndTime();
        carDriverScheduleDTO.setCarDeviceStatus(0);
        Page<ApplyCarOrderAndDriverVO> page = getCarDriverScheduleVOPage(carDriverScheduleDTO);
        Iterator<ApplyCarOrderAndDriverVO> iterator = getCarDriverScheduleVOIterator(page);
        CarDriverScheduleSetting scheduleSetting = null;
        List<ApplyCarOrderDerverVO> list = null;
        while (iterator.hasNext()) {
            ApplyCarOrderAndDriverVO carDriver = iterator.next();
            //查询设置排班规则和排班信息
            if (1 == carDriverScheduleDTO.getFlag()) {
                scheduleSetting = this.getOne(new QueryWrapper<CarDriverScheduleSetting>()
                        .eq("car_id", carDriver.getCarId()));
                list = baseMapper.selectScheduleList(null, carDriver.getCarId(), null);
            } else {
                scheduleSetting = this.getOne(new QueryWrapper<CarDriverScheduleSetting>()
                        .eq("driver_id", carDriver.getDriverId()));
                list = baseMapper.selectScheduleList(null, null, carDriver.getDriverId());
            }
            //先判断不可排班规则
            if (scheduleSetting != null) {
                if (scheduleSetting.getScheduleStatus() != 0) {
                    Integer week = getWeek(startTime);
                    boolean isExits = scheduleSetting.getNotScheduleWeek().contains(week.toString());
                    if (isExits) {
                        iterator.remove();
                        page.setTotal(page.getTotal() - 1);
                        continue;
                    }
                    //临时不可排班
                    boolean conflict = isAllotConflict(scheduleSetting.getStartTime(), scheduleSetting.getEndTime(), startTime, endTime);
                    if (conflict) {
                        iterator.remove();
                        page.setTotal(page.getTotal() - 1);
                        continue;
                    }
                } else {
                    iterator.remove();
                    page.setTotal(page.getTotal() - 1);
                    continue;
                }
            }
            //在判断已排班时间
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(schedule -> {
                    boolean conflict = isAllotConflict(schedule.getStaTime(), schedule.getEndTime(), startTime, endTime);
                    if (conflict) {
                        iterator.remove();
                        page.setTotal(page.getTotal() - 1);
                    }
                });
            }
        }
        return page;
    }

    public int carInfoDevicetotal(CarDriverScheduleNoPageDTO carDriverScheduleNoPageDTO) {
        if (1 == carDriverScheduleNoPageDTO.getFlag()) {
            Integer carInfoTotal = carInfoService.selectCarPageTotal(carDriverScheduleNoPageDTO.getEtpId(), carDriverScheduleNoPageDTO.getLicCodeOrriverName(), carDriverScheduleNoPageDTO.getScheduleStatus());//车辆排班总数
            return carInfoTotal;
        } else {
            Integer carDriverTotal = carDriverInfoService.selectNoDriverInfoPageTotal(carDriverScheduleNoPageDTO.getEtpId(), carDriverScheduleNoPageDTO.getLicCodeOrriverName(), carDriverScheduleNoPageDTO.getScheduleStatus());//司机排班总数
            return carDriverTotal;
        }
    }

    /**
     * jinzhu
     * 不分页迭代器
     * @param list
     * @return
     */
    private Iterator<ApplyCarOrderAndDriverVO> getCarDriverScheduleVOIteratorList(List<ApplyCarOrderAndDriverVO> list) {
        Iterator<ApplyCarOrderAndDriverVO> iterator = list.iterator();
        return iterator;
    }

    /**
     * 查询车辆和司机信息  不分页
     *
     * @param carDriverScheduleNoPageDTO
     * @return
     */
    private List<ApplyCarOrderAndDriverVO> getCarDriverScheduleVO(CarDriverScheduleNoPageDTO carDriverScheduleNoPageDTO) {
        CarDriverScheduleDTO  carDriverScheduleDTO= Cp.cp(carDriverScheduleNoPageDTO, new CarDriverScheduleDTO());
        getEtpId(carDriverScheduleDTO);
        List<ApplyCarOrderAndDriverVO> list = null;
        //车辆排班查询
        if (carDriverScheduleNoPageDTO.getFlag() != null && carDriverScheduleNoPageDTO.getFlag() == 1) {
            list = carInfoService.selectCarList(carDriverScheduleNoPageDTO);
        } else {
            //司机排班查询
            list = carDriverInfoService.selectNoDriverInfoList(carDriverScheduleNoPageDTO);
        }

        return list;
    }

    /**
     *  查询车辆和司机信息  不分页  迭代器算出所有车辆司机总数
     * @param carDriverScheduleNoPageDTO
     * @param carDriverScheduleDTO
     * @return
     */
    public int iteratorList(CarDriverScheduleNoPageDTO carDriverScheduleNoPageDTO,
                         CarDriverScheduleDTO carDriverScheduleDTO){
    List<ApplyCarOrderAndDriverVO> lists = getCarDriverScheduleVO(carDriverScheduleNoPageDTO);
    List<ApplyCarOrderDerverVO> list = null;
    Iterator<ApplyCarOrderAndDriverVO> iteratorList = getCarDriverScheduleVOIteratorList(lists);
    //排班数的累加总和
    int count = 0;
    int count1 = 0;
        while (iteratorList.hasNext()) {
            ApplyCarOrderAndDriverVO carDriverScheduleVO = iteratorList.next();
            if (1 == carDriverScheduleNoPageDTO.getFlag()) {
                if (carDriverScheduleVO.getCarId() != null) {
                    count1++;
                }
                carDriverScheduleDTO.setTotals(carInfoDevicetotal(carDriverScheduleNoPageDTO));//排班的累加
                list = baseMapper.selectScheduleList(carDriverScheduleDTO.getQueryTime(), carDriverScheduleVO.getCarId(), null);
            } else {
                if (carDriverScheduleVO.getDriverId() != null) {
                    count1++;
                }
                carDriverScheduleDTO.setTotals(carInfoDevicetotal(carDriverScheduleNoPageDTO));
                list = baseMapper.selectScheduleList(carDriverScheduleDTO.getQueryTime(), null, carDriverScheduleVO.getDriverId());
            }

            carDriverScheduleVO.setCarDriverSchedules(list);
            Integer scheduleStatus = carDriverScheduleNoPageDTO.getScheduleStatus();
            if (scheduleStatus != null) {
//                if (carDriverScheduleVO.getStatusNo() == 0) {
                if (scheduleStatus == 1) {
                    if (!CollectionUtils.isEmpty(list)) {
                        carDriverScheduleVO.setCarDriverSchedules(list);
                    } else {
                        iteratorList.remove();
                        count++;
                    }
                } else if (scheduleStatus == 0) {
                    if (CollectionUtils.isEmpty(list)) {
                        carDriverScheduleVO.setCarDriverSchedules(list);
                    } else {
                        iteratorList.remove();
                        count++;
                    }
                }
            }
        }
    return  count;
}
    @Override
    public Page<ApplyCarOrderAndDriverVO> getCarDriverSchedulePage(CarDriverScheduleDTO carDriverScheduleDTO) {
        List<ApplyCarOrderDerverVO> list = null;
        CarDriverScheduleNoPageDTO carDriverScheduleNoPageDTO = Cp.cp(carDriverScheduleDTO, new CarDriverScheduleNoPageDTO());
        Page<ApplyCarOrderAndDriverVO> page = getCarDriverScheduleVOPage(carDriverScheduleDTO);
        Iterator<ApplyCarOrderAndDriverVO> iterator = getCarDriverScheduleVOIterator(page);
        if (iterator == null) return null;
        int count = 0;
        int count1 = 0;
       if(iterator.hasNext() == false){  //避免前端刷新的时候,重置车和司机总数
           count=  iteratorList(carDriverScheduleNoPageDTO,carDriverScheduleDTO);
       }
        while (iterator.hasNext()) {
            ApplyCarOrderAndDriverVO carDriverScheduleVO = iterator.next();
            if (1 == carDriverScheduleDTO.getFlag()) {
//                page.setTotal(carInfoDevicetotal(carDriverScheduleDTO));
                if (carDriverScheduleVO.getCarId() != null) {
                    count1++;
                }
                carDriverScheduleDTO.setTotals(carInfoDevicetotal(carDriverScheduleNoPageDTO));
                list = baseMapper.selectScheduleList(carDriverScheduleDTO.getQueryTime(), carDriverScheduleVO.getCarId(), null);
            } else {
                if (carDriverScheduleVO.getDriverId() != null) {
                    count1++;
                }
//                page.setTotal(carInfoDevicetotal(carDriverScheduleDTO));
                carDriverScheduleDTO.setTotals(carInfoDevicetotal(carDriverScheduleNoPageDTO));
                list = baseMapper.selectScheduleList(carDriverScheduleDTO.getQueryTime(), null, carDriverScheduleVO.getDriverId());
            }
            carDriverScheduleVO.setCarDriverSchedules(list);
            Integer scheduleStatus = carDriverScheduleDTO.getScheduleStatus();
            if (scheduleStatus != null) {
//                if (carDriverScheduleVO.getStatusNo() == 0) {
                if (scheduleStatus == 1) {
                    if (!CollectionUtils.isEmpty(list)) {
                        carDriverScheduleVO.setCarDriverSchedules(list);
                    } else {
                        iterator.remove();
                        count++;
                    }
                } else if (scheduleStatus == 0) {
                    if (CollectionUtils.isEmpty(list)) {
                        carDriverScheduleVO.setCarDriverSchedules(list);
                    } else {
                        iterator.remove();
                        count++;
                    }
                }
                if (scheduleStatus == 3) {
                    if (carDriverScheduleVO.getStatusNo() == 1) {
                        carDriverScheduleVO.setCarDriverSchedules(list);
                    } else {
                        iterator.remove();
                        count++;
                    }
                }

            }

        }
        if (count == 0) {
            carDriverScheduleDTO.setTotals(carDriverScheduleDTO.getTotals() - count);
        }
        if (carDriverScheduleDTO.getScheduleStatus() != null) {
            if (carDriverScheduleDTO.getScheduleStatus() == 1) {
                carDriverScheduleDTO.setTotals(count1 - count);
            }
            if (carDriverScheduleDTO.getScheduleStatus() == 0) {
                carDriverScheduleDTO.setTotals(carDriverScheduleDTO.getTotals() - count);
            }
        }
        return page;

    }


    /**
     * 获取正在使用的车辆id
     *
     * @return
     */
    @Override
    public List<Integer> getUsingCarIds() {
        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();
        List<Integer> list = baseMapper.getUsingCarIds(etpId);
        return list;
    }

    /**
     * 获取企业id
     *
     * @param deviceDTO
     */
    private void getEtpId(CarDriverScheduleDTO deviceDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (deviceDTO.getEtpId() == null && user != null) {
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                deviceDTO.setEtpId(user.getEtpId());
            }
        }
    }

    /**
     * 查询车辆和司机信息
     *
     * @param carDriverScheduleDTO
     * @return
     */
    private Page<ApplyCarOrderAndDriverVO> getCarDriverScheduleVOPage(CarDriverScheduleDTO carDriverScheduleDTO) {
        getEtpId(carDriverScheduleDTO);
        Page<ApplyCarOrderAndDriverVO> page = null;
//        if (carDriverScheduleDTO.getScheduleStatus() != null && carDriverScheduleDTO.getScheduleStatus() == 0) {
//            carDriverScheduleDTO.setScheduleStatus(2);
//        }
        //车辆排班查询
        if (carDriverScheduleDTO.getFlag() != null && carDriverScheduleDTO.getFlag() == 1) {
            page = carInfoService.selectCarPage(carDriverScheduleDTO);
        } else {
            //司机排班查询
            page = carDriverInfoService.selectNoDriverInfoPage(carDriverScheduleDTO);
        }
        return page;
    }

    private Iterator<ApplyCarOrderAndDriverVO> getCarDriverScheduleVOIterator(Page<ApplyCarOrderAndDriverVO> page) {
        if (page == null) {
            return null;
        }
        Iterator<ApplyCarOrderAndDriverVO> iterator = page.getRecords().iterator();
        page.setTotal(page.getRecords().size());
        return iterator;
    }

    /**
     * 判断需要排班的车辆或司机时间是否重叠
     *
     * @param settingStartTime1
     * @param settingEndTime1
     * @param startTime
     * @param endTime
     * @return
     */
    private static boolean isAllotConflict(LocalDateTime settingStartTime1, LocalDateTime settingEndTime1, String startTime, String endTime) {
        boolean flag = false;
        if (settingStartTime1 != null && settingEndTime1 != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String settingStartTime = formatter.format(settingStartTime1);
            String settingEndTime = formatter.format(settingEndTime1);
            if (StringUtils.isNotBlank(settingStartTime) || StringUtils.isNotBlank(settingEndTime)) {
                if (startTime.compareTo(settingEndTime) <= 0 && endTime.compareTo(settingStartTime) >= 0) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 根据时间获取星期
     *
     * @param dates
     */
    public static Integer getWeek(String dates) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = f.parse(dates);
        } catch (ParseException e) {
            log.error("根据时间获取星期失败", e);
        }
        cal.setTime(d);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w == 0) {
            w = 7;
        }
        return w;
    }
}
