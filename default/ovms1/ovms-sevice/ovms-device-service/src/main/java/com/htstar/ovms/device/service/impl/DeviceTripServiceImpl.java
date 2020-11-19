package com.htstar.ovms.device.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.feign.EtpInfoFeign;
import com.htstar.ovms.admin.api.vo.EtpInfoSVo;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.device.api.dto.*;
import com.htstar.ovms.device.api.entity.CarViolations;
import com.htstar.ovms.device.api.entity.DeviceTrip;
import com.htstar.ovms.device.api.vo.*;
import com.htstar.ovms.device.mapper.DeviceTripMapper;
import com.htstar.ovms.device.mongo.model.ObdGpsDataMG;
import com.htstar.ovms.device.protoco.GpsItemTp;
import com.htstar.ovms.device.protoco.ObdGpsDataTp;
import com.htstar.ovms.device.protoco.ObdStatDataTp;
import com.htstar.ovms.device.service.CarViolationsService;
import com.htstar.ovms.device.service.DeviceService;
import com.htstar.ovms.device.service.DeviceTripService;
import com.htstar.ovms.device.service.GpsService;
import com.htstar.ovms.device.util.DateXQ;
import com.htstar.ovms.device.util.GpsNewUtil;
import com.htstar.ovms.device.util.ObdUtil;
import com.htstar.ovms.enterprise.api.dto.CarSchedulingTimeWhereDTO;
import com.htstar.ovms.enterprise.api.feign.CarSchedulingTimeFeign;
import com.htstar.ovms.msg.api.constant.MsgTypeConstant;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.Null;
import org.omg.CORBA.Object;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * 行程表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@AllArgsConstructor
@Service
@Slf4j
public class DeviceTripServiceImpl extends ServiceImpl<DeviceTripMapper, DeviceTrip> implements DeviceTripService {

    @Autowired
    private GpsService gpsService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CarViolationsService carViolationsService;

    @Autowired
    DeviceService deviceService;


    @Autowired
    CarSchedulingTimeFeign carSchedulingTimeFeign;
    @Autowired
    EtpInfoFeign etpInfoFeign;

    /**
     * Description: 获取最后一次行程（更新行程使用，时间为点火时间）
     * Author: flr
     * Date: 2020-06-16
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public DeviceTrip getLastTrip(String deviceSn, LocalDateTime lastAcconTime) {
        return baseMapper.getLastTrip(deviceSn, lastAcconTime);
    }

    @Override
    public DeviceTripCountTotalVO getTripInfoByDeviceSnPage(DeviceTripDTO deviceTripDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (deviceTripDTO.getEtpId() == null && user != null) {//pc端企业判断
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                deviceTripDTO.setEtpId(user.getEtpId());
            }else{
                deviceTripDTO.setEtpId(null);
            }
        }
        if(Objects.equals(deviceTripDTO.getEtpId(),null))deviceTripDTO.setEtpId(0);
        List<EtpInfoSVo> currentAndParents1 = etpInfoFeign.getCurrentAndParents1(deviceTripDTO.getEtpId());
        currentAndParents1.forEach(etpInfoSVo -> {
            deviceTripDTO.setEtpIds(etpInfoSVo.getIds());
        });
        if (deviceTripDTO.getEtpId() != null){// APP端企业判断
            if(deviceTripDTO.getEtpId() == CommonConstants.ETP_ID_1) deviceTripDTO.setEtpId(null);
        }
        DeviceTripTotalDTO deviceTripTotalDTO =  new DeviceTripTotalDTO();
        BeanUtils.copyProperties(deviceTripDTO,deviceTripTotalDTO);
        Page<DeviceTripVO> page = baseMapper.getTripInfoByDeviceSnPage(deviceTripDTO);
        DeviceTripCountTotalVO pageTotal = baseMapper.getTripInfoByDeviceSnTotal(deviceTripTotalDTO);
        List<DeviceTripVO> list = page.getRecords();
        LocalDateTime now = OvmDateUtil.getCstNow();
        if (list != null && !list.isEmpty()) {
            pageTotal.setCurrent(deviceTripDTO.getCurrent());
            pageTotal.setSize(deviceTripDTO.getSize());
            pageTotal.setDeviceTrips(list);
            pageTotal.setTotal(page.getTotal());
            List<DeviceTrip> trips = new ArrayList<>();
            for (DeviceTripVO vo : list) {
                String[] split = StrUtil.split(vo.getEndLatlon(), ",");
                Double lat = Double.valueOf(Double.parseDouble(split[0]));
                Double lng = Double.valueOf(Double.parseDouble(split[1]));
                if (StrUtil.isNotBlank(vo.getEndAddr())) {
                    //日期时间转换时间戳
                    long sta = vo.getStaTime().toEpochSecond(ZoneOffset.of("+8"));
                    long end = now.toEpochSecond(ZoneOffset.of("+8"));
                    long l = end - sta;  //判断时间戳是否大于120  和 小于 300 秒
                    if (StrUtil.isNotBlank(vo.getEndAddr()) && (l > 120 && l < 300)) {
                        String endAddr = gpsService.getGpsAddr(lat, lng);
                        vo.setEndAddr(endAddr);
                        DeviceTrip trip = new DeviceTrip();
                        trip.setId(vo.getId());
                        trip.setEndAddr(endAddr);
                        trips.add(trip);
                    }
                } else {
                    String endAddr = gpsService.getGpsAddr(lat, lng);
                    vo.setEndAddr(endAddr);
                    DeviceTrip trip = new DeviceTrip();
                    trip.setId(vo.getId());
                    trip.setEndAddr(endAddr);
                    trips.add(trip);
                }

            }

            if (!trips.isEmpty()) {
                this.updateBatchById(trips);
            }
        }
        return pageTotal;
    }
    public DeviceTripCountTotalVO getTripInfoByDeviceSn(DeviceTripListDTO deviceTripDTO) {
        OvmsUser user = SecurityUtils.getUser();
        List<Integer> li = new ArrayList<>();
        if (deviceTripDTO.getEtpId() == null && user != null) {//pc端企业判断
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                deviceTripDTO.setEtpId(user.getEtpId());
                li.add(user.getEtpId());
                deviceTripDTO.setEtpIds(li);
            }else{
                li.add(1);
                deviceTripDTO.setEtpId(null);
                deviceTripDTO.setEtpIds(li);
            }
        }

        if (deviceTripDTO.getEtpId() != null){// APP端企业判断
            if(deviceTripDTO.getEtpId() == CommonConstants.ETP_ID_1) deviceTripDTO.setEtpId(null);
        }
        DeviceTripTotalDTO deviceTripTotalDTO =  new DeviceTripTotalDTO();
        BeanUtils.copyProperties(deviceTripDTO,deviceTripTotalDTO);
        List<DeviceTripVO> page = baseMapper.getTripInfoByDeviceSn(deviceTripDTO);
        DeviceTripCountTotalVO pageTotal = baseMapper.getTripInfoByDeviceSnTotal(deviceTripTotalDTO);
        List<DeviceTripVO> list = page;
        LocalDateTime now = OvmDateUtil.getCstNow();
        if (list != null && !list.isEmpty()) {
            pageTotal.setDeviceTrips(list);
            List<DeviceTrip> trips = new ArrayList<>();
            for (DeviceTripVO vo : list) {
                String[] split = StrUtil.split(vo.getEndLatlon(), ",");
                Double lat = Double.valueOf(Double.parseDouble(split[0]));
                Double lng = Double.valueOf(Double.parseDouble(split[1]));
                if (StrUtil.isNotBlank(vo.getEndAddr())) {
                    //日期时间转换时间戳
                    long sta = vo.getStaTime().toEpochSecond(ZoneOffset.of("+8"));
                    long end = now.toEpochSecond(ZoneOffset.of("+8"));
                    long l = end - sta;  //判断时间戳是否大于120  和 小于 300 秒
                    if (StrUtil.isNotBlank(vo.getEndAddr()) && (l > 120 && l < 300)) {
                        String endAddr = gpsService.getGpsAddr(lat, lng);
                        vo.setEndAddr(endAddr);
                        DeviceTrip trip = new DeviceTrip();
                        trip.setId(vo.getId());
                        trip.setEndAddr(endAddr);
                        trips.add(trip);
                    }
                } else {
                    String endAddr = gpsService.getGpsAddr(lat, lng);
                    vo.setEndAddr(endAddr);
                    DeviceTrip trip = new DeviceTrip();
                    trip.setId(vo.getId());
                    trip.setEndAddr(endAddr);
                    trips.add(trip);
                }

            }

            if (!trips.isEmpty()) {
                this.updateBatchById(trips);
            }
        }
        return pageTotal;
    }
    /**
     * 获取企业id
     *
     * @param deviceDTO
     */
    private void getEtpId(DeviceTripDTO deviceDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (deviceDTO.getEtpId() == null && user != null) {
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                deviceDTO.setEtpId(user.getEtpId());
            }
        }
    }

    /**
     * Description: 处理协议行程
     * Author: flr
     * Company: 航通星空
     * Modified By:
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer dealProcotoTrip(String deviceSn, ObdGpsDataTp obdGpsDataTp, ObdStatDataTp obdStatDataTp) {
        String licCode = deviceService.getLicCode(deviceSn);
        CarSchedulingTimeWhereDTO cardto = new CarSchedulingTimeWhereDTO();
        LocalTime lastAcconTime = LocalDateTime.now().toLocalTime();
        cardto.setStatime(lastAcconTime.toString());
        cardto.setEndtime(lastAcconTime.toString());
        cardto.setLicCode(licCode);
        cardto.setNotScheduleWeek(DateXQ.XQ());
        int  count= carSchedulingTimeFeign.getlicCodeCount(licCode, SecurityConstants.FROM_IN);//判断排班是否有车
        int  count1= carSchedulingTimeFeign.getCount(cardto,SecurityConstants.FROM_IN);//判断车辆是否排班
        //正常上传的GPS数据需要更新到行程和LAST表中
        DeviceTrip deviceTrip = this.getLastTrip(deviceSn, obdStatDataTp.getLastAcconTime());
        CarViolations carViolations = new CarViolations();
        //那最后一条GPS
        GpsItemTp lastGpsItem = obdGpsDataTp.getLastGpsItemTp();
        if (deviceTrip != null) {
            //已有行程，更新最后位置
            deviceTrip.setDeviceSn(deviceSn);
            deviceTrip.setEndTime(lastGpsItem.getGpsTime());
            deviceTrip.setEndLatlon(lastGpsItem.getLat() + "," + lastGpsItem.getLng());
            deviceTrip.setMileage(obdStatDataTp.getCurrentTripMileage());
            deviceTrip.setFuelConsumption(NumberUtil.toBigDecimal(obdStatDataTp.getCurrentFuel()));
            deviceTrip.setTotalMileage(obdStatDataTp.getTotalTripMileage());
            deviceTrip.setTotalFuel(NumberUtil.toBigDecimal(obdStatDataTp.getTotalFuel()));
            //更新违规地址
            UpdateWrapper<CarViolations> mapper = new UpdateWrapper<>();
            mapper.lambda().eq(CarViolations::getDeviceSn,deviceSn);
            mapper.orderByDesc("id").last("limit 1");
            if(count != 0){
            if(count1 == 0) {
                carViolations.setDeviceSn(deviceSn);
                carViolations.setEndTime(lastGpsItem.getGpsTime());
                carViolationsService.update(carViolations, mapper);
                }
            }

            this.updateById(deviceTrip);
            return deviceTrip.getId();
        } else {
            //新行程入库
            DeviceTrip insertTrip = new DeviceTrip();
            insertTrip.setDeviceSn(deviceSn);
            insertTrip.setStaTime(obdStatDataTp.getLastAcconTime());
            insertTrip.setEndTime(obdStatDataTp.getLastAcconTime());
            insertTrip.setEndLatlon(lastGpsItem.getLat() + "," + lastGpsItem.getLng());
            insertTrip.setMileage(obdStatDataTp.getCurrentTripMileage());
            insertTrip.setFuelConsumption(NumberUtil.toBigDecimal(obdStatDataTp.getCurrentFuel()));
            insertTrip.setTotalMileage(obdStatDataTp.getTotalTripMileage());
            insertTrip.setTotalFuel(NumberUtil.toBigDecimal(obdStatDataTp.getTotalFuel()));
            insertTrip.setStaLatlon(lastGpsItem.getLat() + "," + lastGpsItem.getLng());
            //逆地理位置解析
            String staAddr = gpsService.getGpsAddr(lastGpsItem.getLat(), lastGpsItem.getLng());
            if(count != 0){
                if(count1 == 0) {
                carViolations.setDeviceSn(deviceSn);
                carViolations.setStaAddr(staAddr);
                carViolations.setStaTime(obdStatDataTp.getLastAcconTime());
                carViolations.setEndTime(obdStatDataTp.getLastAcconTime());
                carViolationsService.save(carViolations);
                }
            }
            insertTrip.setStaAddr(staAddr);
            this.save(insertTrip);
            return insertTrip.getId();
        }
    }

    /**
     * Description: 对行程的报警进行统计
     * Author: flr
     * Date: ${DATE}
     * Company: 航通星空
     * Modified By:
     */
    @Override
    public boolean updateAlarmCount(DeviceTrip deviceTrip) {
        this.baseMapper.updateAlarmCount(deviceTrip);
        return true;
    }

    @Override
    public Page<TripHistoricalVO> getTripHistoricalPage(TripHistoricalDTO tripHistoricalDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (tripHistoricalDTO.getEtpId() == null && user != null) {
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                tripHistoricalDTO.setEtpId(user.getEtpId());
            }
        }
        List<TripHistoricalVO> list = new ArrayList<>();
        Page<TripHistoricalVO> page = baseMapper.getTripHistoricalPage(tripHistoricalDTO);
        page.getRecords().forEach(tripHistoricalVO -> {
            tripHistoricalDTO.setDeviceSn(tripHistoricalVO.getDeviceSn());
            if (tripHistoricalVO.getDeviceSn() != null) {
                for (int i = 0; i < tripHistoricalDTO.getSelectDates().length; i++) {
                    if (tripHistoricalDTO.getSelectDates()[i] == 1) {
                        tripHistoricalDTO.setSelectDate(1);
                        Integer dateTripPage = baseMapper.getDateTripPages(tripHistoricalDTO.getDeviceSn(),tripHistoricalDTO.getSelectDate(),tripHistoricalDTO.getDayTime());
                        if (dateTripPage != null) {
                            tripHistoricalVO.setDayTimeLong(String.valueOf(dateTripPage));
                        } else {
                            tripHistoricalVO.setDayTimeLong("0");
                        }
                    }
                    if (tripHistoricalDTO.getSelectDates()[i] == 2) {
                        tripHistoricalDTO.setSelectDate(2);
                        Integer dateTripPage = baseMapper.getDateTripPages(tripHistoricalDTO.getDeviceSn(),tripHistoricalDTO.getSelectDate(),tripHistoricalDTO.getDayTime());
                        if (dateTripPage != null) {
                            tripHistoricalVO.setMonthTimeLong(String.valueOf(dateTripPage));
                        } else {
                            tripHistoricalVO.setMonthTimeLong("0");
                        }
                    }
                    if (tripHistoricalDTO.getSelectDates()[i] == 3) {
                        tripHistoricalDTO.setSelectDate(3);
                        Integer dateTripPage = baseMapper.getDateTripPages(tripHistoricalDTO.getDeviceSn(),tripHistoricalDTO.getSelectDate(),tripHistoricalDTO.getDayTime());
                        if (dateTripPage != null) {
                            tripHistoricalVO.setYearTimeLong(String.valueOf(dateTripPage));
                        } else {
                            tripHistoricalVO.setYearTimeLong("0");
                        }
                    }

                }
            } else {
                tripHistoricalVO.setMonthTimeLong("0");
                tripHistoricalVO.setDayTimeLong("0");
                tripHistoricalVO.setYearTimeLong("0");
            }
            list.add(tripHistoricalVO);
        });
        page.setRecords(list);//映射重新放进page 进行显示
        return page;
    }

    @Override
    public Page<DeviceTripVO> getDateTripPage(TripHistoricalDTO tripHistoricalDTO) {
        return baseMapper.getDateTripPage(tripHistoricalDTO);
    }

    /**
     * 获取每个月的天数
     *
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Override
    public StatisticsTripVO getStatisticsTrip(String deviceSn, String monthTime) {
        LocalDateTime parse = LocalDateTime.parse(monthTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int month = parse.getMonth().getValue();
        int daysOfMonth = 0;
        try {
            daysOfMonth = getDaysOfMonth(sdf.parse(monthTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<StatisticsMileageVO> mileageVOS1 = new ArrayList<>();
        List<StatisticsElapsedTimeVO> mileageVOS2 = new ArrayList<>();
        List<StatisticsFuelConsumptionVO> mileageVOS3 = new ArrayList<>();
        StatisticsTripVO statisticsData = baseMapper.getStatisticsData(deviceSn, monthTime);

        if (statisticsData != null) {
            //每日平均值
            statisticsData.setAvgMileage(statisticsData.getTotoalMileage() * 1.0 / month);
            statisticsData.setAvgLongTime(statisticsData.getTotoalLongTIme() * 1.0 / month);
            statisticsData.setAvgFuelConsumption(statisticsData.getTotoalFuelConsumption() * 1.0 / month);
            List<StatisticsMileageVO> mileageVOS = baseMapper.getMontMileage(deviceSn, monthTime);
            List<StatisticsElapsedTimeVO> elapsedTimeVOS = baseMapper.getMontElapsedTime(deviceSn, monthTime);
            List<StatisticsFuelConsumptionVO> fuelConsumptionVOS = baseMapper.getMontFuelConsumption(deviceSn, monthTime);
            for (int i = 1; i <= daysOfMonth; i++) {
                //获取里程
                int day = i;
                StatisticsMileageVO vo = new StatisticsMileageVO();
                statisticsData.setStatisticsMileageVOS(mileageVOS);
                Optional<StatisticsMileageVO> to = mileageVOS
                        .stream()
                        .filter(statisticsMileageVO -> statisticsMileageVO.getGetTime() == day)
                        .findFirst();
                vo.setMileage(to.isPresent() ? to.get().getMileage() : 0);
                vo.setGetTime(day);
                mileageVOS1.add(vo);
                statisticsData.setStatisticsMileageVOS(mileageVOS1);

                //获取耗时
                StatisticsElapsedTimeVO vo1 = new StatisticsElapsedTimeVO();
                statisticsData.setStatisticsElapsedTimeVOS(elapsedTimeVOS);
                Optional<StatisticsElapsedTimeVO> to1 = elapsedTimeVOS
                        .stream()
                        .filter(statisticsElapsedTimeVO -> statisticsElapsedTimeVO.getGetTime() == day)
                        .findFirst();
                vo1.setLongTime(to1.isPresent() ? to1.get().getLongTime() : 0);
                vo1.setGetTime(day);
                mileageVOS2.add(vo1);


                statisticsData.setStatisticsElapsedTimeVOS(mileageVOS2);

                //获取油耗
                StatisticsFuelConsumptionVO vo2 = new StatisticsFuelConsumptionVO();
                statisticsData.setStatisticsFuelConsumptionVOS(fuelConsumptionVOS);
                Optional<StatisticsFuelConsumptionVO> to2 = fuelConsumptionVOS
                        .stream()
                        .filter(statisticsFuelConsumptionVO -> statisticsFuelConsumptionVO.getGetTime() == day)
                        .findFirst();
                vo2.setFuelConsumption(to2.isPresent() ? to2.get().getFuelConsumption() : 0);
                vo2.setGetTime(day);
                mileageVOS3.add(vo2);
                statisticsData.setStatisticsFuelConsumptionVOS(mileageVOS3);
            }
            return statisticsData;
        } else {
            //每日平均值
            StatisticsTripVO statisticsTripVO = new StatisticsTripVO();
            statisticsTripVO.setTotoalLongTIme(0);
            statisticsTripVO.setTotoalFuelConsumption(0);
            statisticsTripVO.setTotoalMileage(0);
            statisticsTripVO.setAvgMileage(0.00);
            statisticsTripVO.setAvgLongTime(0.00);
            statisticsTripVO.setAvgFuelConsumption(0.00);
            for (int i = 1; i <= daysOfMonth; i++) {
                //获取里程
                int day = i;
                StatisticsMileageVO vo = new StatisticsMileageVO();
                vo.setMileage(0);
                vo.setGetTime(day);
                mileageVOS1.add(vo);
                statisticsTripVO.setStatisticsMileageVOS(mileageVOS1);
                //获取耗时
                StatisticsElapsedTimeVO vo1 = new StatisticsElapsedTimeVO();
                vo1.setLongTime(0);
                vo1.setGetTime(day);
                mileageVOS2.add(vo1);
                statisticsTripVO.setStatisticsElapsedTimeVOS(mileageVOS2);
                //获取油耗
                StatisticsFuelConsumptionVO vo2 = new StatisticsFuelConsumptionVO();
                vo2.setFuelConsumption(0);
                vo2.setGetTime(day);
                mileageVOS3.add(vo2);
                statisticsTripVO.setStatisticsFuelConsumptionVOS(mileageVOS3);
            }
            return statisticsTripVO;
        }
    }

    @Override
    public List<TripAndCarInfoVO> exportTripByEtpAndLicCode(TripInfoExportDTO tripExportDTO) {
        return baseMapper.exportTripByEtpAndLicCode(tripExportDTO);
    }

    @Override
    public TripPlaybackVO getGPSDataByDeviceSn(TripGpsDTO tripGpsDTO) {
        TripPlaybackVO tripPlaybackVO = new TripPlaybackVO();
        tripPlaybackVO.setStaLatlon(tripGpsDTO.getStaLatlon());
        tripPlaybackVO.setEndLatlon(tripGpsDTO.getEndLatlon());
        //查询轨迹的每个点
        if (StringUtils.isNotBlank(tripGpsDTO.getDeviceSn())) {
            Query query = new Query(Criteria.where("deviceSn").is(tripGpsDTO.getDeviceSn()));
            if (StringUtils.isNotBlank(tripGpsDTO.getStartTime())) {
                query = new Query(Criteria.where("deviceSn").is(tripGpsDTO.getDeviceSn()).and("gpsTime").gte(subtractEightDate(tripGpsDTO.getStartTime())));
            }
            if (StringUtils.isNotBlank(tripGpsDTO.getEndTime())) {
                query = new Query(Criteria.where("deviceSn").is(tripGpsDTO.getDeviceSn()).and("gpsTime").lte(tripGpsDTO.getEndTime()));
            }
            if (StringUtils.isNotBlank(tripGpsDTO.getStartTime()) && StringUtils.isNotBlank(tripGpsDTO.getEndTime())) {
                query = new Query(Criteria.where("deviceSn").is(tripGpsDTO.getDeviceSn()).andOperator(
                        Criteria.where("gpsTime").gte(subtractEightDate(tripGpsDTO.getStartTime())),
                        Criteria.where("gpsTime").lte(subtractEightDate(tripGpsDTO.getEndTime()))));
            }
            List<ObdGpsDataMG> list = mongoTemplate.find(query, ObdGpsDataMG.class);
            List<LatAndLngVO> latAndLngVOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(list)) {
                for (ObdGpsDataMG gpsDataMG : list) {
                    LatAndLngVO latAndLngVO = new LatAndLngVO();
                    //WGS84坐标转换GCJ-02坐标  （国内通用是WGS84 高德是GCJ-02）
                    double[] doubles = GpsNewUtil.toGCJ02Point(gpsDataMG.getLat(), gpsDataMG.getLng(), 5);
                    latAndLngVO.setLat(doubles[0]);
                    latAndLngVO.setLng(doubles[1]);
                    latAndLngVOS.add(latAndLngVO);
                }
            }
            tripPlaybackVO.setLatAndLngVOList(latAndLngVOS);
        }
        return tripPlaybackVO;
    }

    @Override
    public Page<TripReportFormsVO> getTripReportForms(TripReportFormsDTO tripReportFormsDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (tripReportFormsDTO.getEtpId() == null && user != null) {
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                tripReportFormsDTO.setEtpId(user.getEtpId());
            }
        }
        return baseMapper.getTripReportForms(tripReportFormsDTO);
    }

    @Override
    public List<TripReportFormsVO> exportTripReportForms(ExportTripReportFormsDTO exportTripReportFormsDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (exportTripReportFormsDTO.getEtpId() == null && user != null) {
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                exportTripReportFormsDTO.setEtpId(user.getEtpId());
            }
        }
        return baseMapper.exportTripReportForms(exportTripReportFormsDTO);
    }

    /**
     * 转化时间格式
     *
     * @param dateStr
     * @return
     */
    private static Date subtractEightDate(String dateStr) {
        Date parse = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
//            Date date = format.parse(dateStr);
//            减去8小时，因为mongodb存储格式UTC 比北京时间晚8小时，这里用的数据是北京时间
//            long rightTime = (date.getTime() - 8 * 60 * 60 * 1000);
//            parse = format.parse(format.format(rightTime));
            parse = format.parse(dateStr);
        } catch (ParseException e) {
            log.error("轨迹回放时间加8小时异常", e);
        }
        return parse;
    }

}
