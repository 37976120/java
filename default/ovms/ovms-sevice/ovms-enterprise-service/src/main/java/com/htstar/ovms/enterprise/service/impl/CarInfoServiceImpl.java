package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.feign.SysDeptFeign;
import com.htstar.ovms.admin.api.vo.CarInfoVO;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.core.util.WebUtils;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.device.api.entity.DriverMapAreaCarInfo;
import com.htstar.ovms.device.api.feign.DeviceMapSysAreaFeign;
import com.htstar.ovms.enterprise.api.dto.*;
import com.htstar.ovms.enterprise.api.entity.CarDevice;
import com.htstar.ovms.enterprise.api.entity.CarInfo;
import com.htstar.ovms.enterprise.api.req.CarItemPageReq;
import com.htstar.ovms.enterprise.api.vo.*;
import com.htstar.ovms.enterprise.mapper.CarInfoMapper;
import com.htstar.ovms.enterprise.service.CarDeviceService;
import com.htstar.ovms.enterprise.service.CarDriverScheduleSettingService;
import com.htstar.ovms.enterprise.service.CarInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 公车表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Service
@Slf4j
public class CarInfoServiceImpl extends ServiceImpl<CarInfoMapper, CarInfo> implements CarInfoService {
    @Autowired
    private SysDeptFeign sysDeptFeign;
    @Autowired
    private CarDeviceService carDeviceService;
    @Autowired
    private CarDriverScheduleSettingService carOrderService;
    @Autowired
    private DeviceMapSysAreaFeign deviceMapSysAreaFeign;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public R saveBaseInfo(CarInfoMapAreaDTO carInfo) {
        log.info("请求参数为{}", carInfo);
        //座位数
        Integer seateNum = carInfo.getSeateNum();
        if (seateNum != null) {
            if (!NumberUtil.isInteger(seateNum.toString())) {
                return R.failed("请输入正确的座位数");
            }
        }

        //车牌号
        String licCode = carInfo.getLicCode();
        if (StrUtil.isEmpty(licCode)){
            return R.failed("请输入车牌号");
        }
        if (licCode.length()>10){
            return R.failed("请输入正确的车牌");
        }
        CarInfo car = baseMapper.selectOne(new QueryWrapper<CarInfo>()
                .eq("lic_code", licCode)
                .eq("del_flag", 0));
        if (car != null) {
            return R.failed("该车牌号已存在");
        }
        //获取登陆用户企业号
        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();
        carInfo.setUserId(user.getId());
        carInfo.setEtpId(etpId);
        CarInfo carInfo1 = new CarInfo();
        BeanUtils.copyProperties(carInfo, carInfo1);
        //创建或者添加 如果先添加了行驶证信息则修改
        if (carInfo.getId() != null && carInfo.getId() > 0) {
            baseMapper.updateById(carInfo1);

        } else {
            baseMapper.insert(carInfo1);
        }
        //需要绑定设备号
        String deviceSn = carInfo.getDeviceSn();
        if (StrUtil.isNotBlank(deviceSn)) {
            //检查设备号是否合理
            R r = carDeviceService.checkDeviceIsBinding(deviceSn);
            if (r.getCode() == CommonConstants.FAIL) {
                return r;
            }
            carDeviceService.save(carInfo1.getId(), deviceSn);
        }
        //添加地图车辆关联标签表
        DriverMapAreaCarInfo info = new DriverMapAreaCarInfo();
        info.setMapAreaCode1(carInfo.getMapAreaCode1());
        info.setMapAreaCode2(carInfo.getMapAreaCode2());
        info.setMapAreaCode3(carInfo.getMapAreaCode3());
        info.setMapAreaName(carInfo.getMapAreaName());
        info.setMapCarInfoId(carInfo1.getId());
        if (carInfo1.getId() > 0 || carInfo1.getId() != null) {
            deviceMapSysAreaFeign.saveBaseMapAreaInfo(info);
        }
        return R.ok(carInfo1.getId(), "保存成功");
    }

    /**
     * 保存驾驶证信息
     *
     * @param carInfo
     * @return
     */
    @Override
    public R saveDrivingLicenseInfo(CarInfo carInfo) {
        log.info("行驶证信息请求参数为{}", carInfo);
        if (carInfo.getId() == null) {
//            int id = baseMapper.insert(carInfo);
//            return R.ok(id,"添加成功");
            return R.failed("请先保存基本信息");
        }
        this.updateById(carInfo);
        return R.ok("保存成功");
    }


    /**
     * 绑定设备
     *
     * @param carId
     * @param deviceSn
     * @return
     */
    @Override
    public R bindingDeviceSn(Integer carId, String deviceSn) {
        if (StrUtil.isBlank(deviceSn)) {
            return R.failed("请输入设备号");
        }
        R r = carDeviceService.checkDeviceIsBinding(deviceSn);
        if (r.getCode() == 1) {
            return r;
        }
        return carDeviceService.save(carId, deviceSn);
    }

    /**
     * 修改基本信息
     *
     * @param carInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateBaseInfo(CarInfoMapAreaDTO carInfo) {
        log.info("修改的车辆信息为{}", carInfo);
        CarInfo oldCarInfo = baseMapper.selectOne(new QueryWrapper<CarInfo>().eq("id", carInfo.getId()));
        //新旧车牌不相同
        String licCode = carInfo.getLicCode();
        if (licCode.length()>10){
            return R.failed("请输入正确的车牌");
        }
        if (!licCode.equals(oldCarInfo.getLicCode())) {
            Integer count = baseMapper.selectCount(new QueryWrapper<CarInfo>().eq("lic_code", oldCarInfo.getLicCode())
                    .eq("del_flag", 0));
            if (count > 0) {
                return R.failed("新车牌号已存在,修改失败");
            }
        }
        CarInfo carInfo1 = new CarInfo();
        BeanUtils.copyProperties(carInfo, carInfo1);
        baseMapper.updateById(carInfo1);
        DriverMapAreaCarInfo info = new DriverMapAreaCarInfo();
        info.setMapAreaCode1(carInfo.getMapAreaCode1());
        info.setMapAreaCode2(carInfo.getMapAreaCode2());
        info.setMapAreaCode3(carInfo.getMapAreaCode3());
        info.setMapAreaName(carInfo.getMapAreaName());
        info.setMapCarInfoId(carInfo1.getId());
        DriverMapAreaCarInfo byUserId = deviceMapSysAreaFeign.getBymMapCarInfoId(carInfo.getId());
        if (byUserId == null) { //如果改车每月绑定地图标签则添加
            if(StringUtils.isNotBlank(carInfo.getMapAreaName())){

                deviceMapSysAreaFeign.saveBaseMapAreaInfo(info);
            }
        } else {
            //如果不想绑定则删除改标签
            if (StringUtils.isBlank(carInfo.getMapAreaName()) ) {
                deviceMapSysAreaFeign.removeByMapCarInfoId(carInfo.getId());
            } else {
                //修改标签
                deviceMapSysAreaFeign.updateBaseMapAreaInfo(info);
            }
        }
        //车辆id
        Integer carInfoId = carInfo.getId();
        //新设备号
        String infoDeviceSn = carInfo.getDeviceSn();
        if (StrUtil.isNotBlank(infoDeviceSn)) {
            //原设备号
            String deviceSn = carDeviceService.getDeviceSnByCarId(carInfoId);
            //原来没有设备号
            if (StrUtil.isBlank(deviceSn)) {
                R r = carDeviceService.checkDeviceIsBinding(infoDeviceSn);
                //设备不符合
                if (r.getCode() == CommonConstants.FAIL) {
                    return r;
                }
                //设备符合就保存
                carDeviceService.save(carInfoId, carInfo.getDeviceSn());
            }
            //原来有设备号
            R updateR = carDeviceService.update(carInfoId, carInfo.getDeviceSn());
            //设备不符合
            if (updateR.getCode() == CommonConstants.FAIL) {
                return updateR;
            }
        }
        //解绑设备号
        if (StrUtil.isBlank(infoDeviceSn)) {
            //原设备号
            String deviceSn = carDeviceService.getDeviceSnByCarId(carInfoId);
            //原来有设备号 删除数据库
            if (StrUtil.isNotBlank(deviceSn)) {
                carDeviceService.remove(new QueryWrapper<CarDevice>().eq("car_id", carInfoId));
            }
        }

        return R.ok("信息修改成功");
    }

    /**
     * 修改驾驶证信息
     *
     * @param carInfo
     * @return
     */
    @Override
    public R updateDrivingLicenseInfo(CarInfo carInfo) {
        baseMapper.updateById(carInfo);
        return R.ok("驾驶证信息修改成功");
    }


    @Override
    public R removeById(Integer id) {
        CarInfo carInfo = baseMapper.selectById(id);
        carInfo.setUpdateTime(LocalDateTime.now());
        carInfo.setDelFlag(1);
        baseMapper.updateById(carInfo);
        return R.ok("删除成功");
    }

    @Override
    public R updateCarInfo(CarInfo carInfo) {
        //待修改车牌号
        String licCode = carInfo.getLicCode();
        CarInfo oldCar = baseMapper.selectById(carInfo.getId());
        //如果新车牌不等于旧车牌 需判断新车牌是否绑定
        if (!oldCar.getLicCode().equals(licCode)) {
            CarInfo carInfo1 = baseMapper.selectOne(new QueryWrapper<CarInfo>().eq("lic_code", licCode));
            if (carInfo1 != null) {
                return R.failed("该车牌已经存在,修改失败");
            }
        }
        baseMapper.updateById(carInfo);
        return R.ok("修改成功");
    }

    /**
     * 档案管理分页
     *
     * @param carInfoDTO
     * @return
     */
    @Override
    public Page<CarInfo> queryPage(CarInfoDTO carInfoDTO) {
        Boolean appClient = SecurityUtils.isAppClient();
        OvmsUser user = SecurityUtils.getUser();

        Integer etpId = user.getEtpId();
        //是app端登陆的
        if (appClient) {
            List<String> roleCodeList = SecurityUtils.getRoleCode(user);
            //非管理员
            if (!roleCodeList.contains(CommonConstants.ROLE_ADMIN)) {
                carInfoDTO.setUserId(user.getId());
            }
        }
        carInfoDTO.setEtpId(etpId);
        Page<CarInfo> carInfoPage = baseMapper.queryPage(carInfoDTO);
        List<CarInfo> records = carInfoPage.getRecords();
        if (CollUtil.isNotEmpty(records)){
            List<Integer> usingCars = baseMapper.getUsingCarIds(etpId);
            //有正在使用的车
            if (CollUtil.isNotEmpty(usingCars)){
               List<CarInfo> carInfos = new ArrayList<>();
                for (CarInfo carInfo : records) {
                    if (usingCars.contains(carInfo.getId())){
                        carInfo.setOrderStatus(0);
                    }
                    carInfos.add(carInfo);
                }
                carInfoPage.setRecords(carInfos);
            }
        }
        return carInfoPage;
    }


    /**
     * 档案管理导出
     *
     * @param carInfoDTO
     */
    @Override
    public void export(CarInfoDTO carInfoDTO) {
        carInfoDTO.setSize(Long.MAX_VALUE);
        Integer etpId = SecurityUtils.getUser().getEtpId();
        //导出数据集合
        List<Map<String, Object>> rows = new ArrayList<>();
        //excel行数据
        carInfoDTO.setEtpId(etpId);
        List<CarInfo> carInfos = baseMapper.exportExcel(carInfoDTO);
        if (carInfos.size() > 0) {
            for (CarInfo carInfo : carInfos) {
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                map.put("车牌号", carInfo.getLicCode() == null ? "" : carInfo.getLicCode());
                map.put("部门", carInfo.getDeptName() == null ? "" : carInfo.getDeptName());
                Integer carLevel = carInfo.getCarLevel();
                String level = "";
                if (carLevel != null) {
                    if (carLevel == 0) {
                        level = "A级(紧凑型车)";
                    } else if (carLevel == 1) {
                        level = "B级（中型车）";
                    } else if (carLevel == 2) {
                        level = "C级（中大型车）";
                    } else if (carLevel == 3) {
                        level = "D级（大型车）";
                    }
                }
                map.put("级别", level);
                map.put("座位数", carInfo.getSeateNum());
                map.put("燃油编号", carInfo.getFuelType() + "号汽油");
                map.put("购车时间", carInfo.getIssuedTime() == null ? "" : DateUtil.format(carInfo.getIssuedTime(), "yyyy-MM-dd"));
                map.put("司机", carInfo.getUsername());
                Integer carStatus = carInfo.getCarStatus();
                String status = "";
                if (carStatus != null) {
                    if (carStatus == 0) {
                        status = "正常";
                    } else if (carStatus == 1) {
                        status = "维修";
                    } else if (carStatus == 2) {
                        status = "保养";
                    } else if (carStatus == 3) {
                        status = "年检";
                    } else if (carStatus == 4) {
                        status = "故障";
                    }
                }
                map.put("车辆状态", status);
                map.put("绑定设备", carInfo.getDeviceSn());
                rows.add(map);
            }
        }
        this.carExportUtil(rows, "车辆信息");

    }


    @Override
    public Page<CarInfo> selectCarInfoPage(CarInfoDTO carInfoDTO) {
        getEtpId(carInfoDTO);
        return baseMapper.selectCarInfoPage(carInfoDTO);
    }

    /**
     * 根据部门id获取部门名称
     *
     * @param deptId
     * @return
     */
    @Override
    public String getCarDeptNameById(Integer deptId) {
        R r = sysDeptFeign.getDeptNameById(deptId, SecurityConstants.FROM_IN);
        String deptName = r.getData().toString();
        return deptName;
    }


    @Override
    public void carExportUtil(List<Map<String, Object>> rows, String fileName) {
        HttpServletResponse response = WebUtils.getResponse();
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        // 一次性写出内容，使用默认样式，强制输出标题
        //writer.merge(rows.size() - 1, fileName);
        writer.write(rows, true);
        //out为OutputStream，需要写出到的目标流

        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        ServletOutputStream out = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
            out = response.getOutputStream();
        } catch (Exception e) {
            log.error("{}导出Excel异常", fileName);
        }
        writer.flush(out, true);
        // 关闭writer，释放内存
        writer.close();
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    @Override
    public CarInfoMapAreaDTO selectCarInfoById(Integer id) {
        return baseMapper.selectCarInfoById(id);
    }

    @Override
    public List<CarInfo> exportCarInfo(CarInfoExportDTO carInfoDTO) {
        CarInfoDTO deviceDTO = new CarInfoDTO();
        if (carInfoDTO.getEtpId() != null) {

            deviceDTO.setEtpId(carInfoDTO.getEtpId());
            getEtpId(deviceDTO);
        }
        List<Integer> list = new ArrayList<>(10);
        if (StringUtils.isNotBlank(carInfoDTO.getIds())) {
            String[] ids = carInfoDTO.getIds().split(",");
            for (String id : ids) {
                list.add(Integer.parseInt(id));
            }
        }
        return baseMapper.exportCarInfo(carInfoDTO, list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateByIds(String ids) {
        for (String id : ids.split(",")) {
            CarInfo carInfo = new CarInfo();
            carInfo.setId(Integer.parseInt(id));
            carInfo.setDelFlag(1);
            baseMapper.updateById(carInfo);
            carDeviceService.remove(new QueryWrapper<CarDevice>().eq("car_id", id));
        }
        return R.ok();
    }

    @Override
    public R selectCarLocationPage(CarLocationNoPageDTO carLocationNoPageDTO) {
        CarLocationDTO carLocationDTO = new CarLocationDTO();
        carLocationDTO.setTotal(carLocationNoPageDTO.getTotal());
        carLocationDTO.setSize(carLocationNoPageDTO.getSize());
        carLocationDTO.setCurrent(carLocationNoPageDTO.getCurrent());
        carLocationDTO.setLicCode(carLocationNoPageDTO.getLicCode());
        CarLocationsVO carLocationsVO = new CarLocationsVO();
        OvmsUser user = SecurityUtils.getUser();
        carLocationDTO.setEtpId(user.getEtpId());
        int count = 0;
        int count1 = 0;
        int count2 = 0;
        CarLocationsLatLngVO vo = null;
        Page<CarLocationVO> carLocationVOPage = baseMapper.selectCarLocationPage(carLocationDTO);
        List<CarLocationsLatLngVO> carLocationsLatLngVOS = baseMapper.selectCarLocations(carLocationNoPageDTO.getLicCode(), user.getEtpId());
        for (int i = 0; i < carLocationsLatLngVOS.size(); i++) {
            vo = carLocationsLatLngVOS.get(i);
            if (vo.getOnline() != null && vo.getOnline() == 1) {
                vo.getOnline();
                count1 = count + i;

            }
            if (vo.getOnline() != null && vo.getOnline() == 0) {
                vo.getOnline();
                count2 = count + i;

            }

        }
        carLocationsVO.setCarLocationVOS(carLocationVOPage);
        carLocationsVO.setCountCarStatus(count2);
        carLocationsVO.setCountCarSends(count1);
        carLocationsVO.setCarLocationsLatLngVOS(carLocationsLatLngVOS);

        return R.ok(carLocationsVO);
    }

    @Override
    public R selectCarLocations(CarLocationNoPageDTO carLocationDTO) {
        CarLocationsVO carLocationsVO = new CarLocationsVO();
        OvmsUser user = SecurityUtils.getUser();
        carLocationDTO.setEtpId(user.getEtpId());
        int count = 0;
        int count1 = 0;
        int count2 = 0;
        CarLocationsLatLngVO vo = null;
        List<CarLocationsLatLngVO> carLocationsLatLngVOS = null;//baseMapper.selectCarLocations(carLocationDTO);
        for (int i = 0; i < carLocationsLatLngVOS.size(); i++) {
            vo = carLocationsLatLngVOS.get(i);
            if (vo.getOnline() != null && vo.getOnline() == 1) {
                vo.getOnline();
                count1 = count + 1;

            }
            if (vo.getOnline() != null && vo.getOnline() == 0) {
                vo.getOnline();
                count2 = count + 1;

            }

        }
        carLocationsVO.setCountCarStatus(count2);
        carLocationsVO.setCountCarSends(count1);
        carLocationsVO.setCarLocationsLatLngVOS(carLocationsLatLngVOS);

        return R.ok(carLocationsVO);
    }

    @Override
    public CarRealTimeDrivingVO selectCarDriving(String deviceSn) {
        return baseMapper.selectCarDriving(deviceSn);
    }


    /**
     * 司机用户名下车辆
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getLicCodeByUser(Integer userId) {
        return baseMapper.getLicCodeByUser(userId);
    }


    @Override
    public List<CarInfoVO> getCarInfoByDeptId(Integer deptId) {
        return baseMapper.getCarInfoByDeptId(deptId);
    }

    /**
     * 获取分页对象 根据用户角色
     *
     * @param carItemPageReq
     * @return
     */
    @Override
    public CarItemPageReq getPageReqByRole(CarItemPageReq carItemPageReq) {
        OvmsUser user = SecurityUtils.getUser();
        Boolean appClient = SecurityUtils.isAppClient();
        Integer userId = user.getId();
        Integer etpId = user.getEtpId();
        //是app端登陆 管理员只需设置企业id
        if (appClient) {
            List<String> roleCodeList = SecurityUtils.getRoleCode(user);
            //判断用户角色
            //非管理员
            if (!roleCodeList.contains(CommonConstants.ROLE_ADMIN)) {
                List<String> licCodes = this.getLicCodeByUser(userId);
                carItemPageReq.setLicCodes(licCodes);
                carItemPageReq.setUserId(userId);
            }
        }
        carItemPageReq.setEtpId(etpId);
        return carItemPageReq;
    }

    @Override
    public Page<ApplyCarOrderAndDriverVO> selectCarPage(CarDriverScheduleDTO carDriverScheduleDTO) {
        return baseMapper.selectCarPage(carDriverScheduleDTO);
    }



    @Override
    public Integer selectCarPageTotal(Integer etpId,String licCodeOrriverName,Integer scheduleStatus) {
        return baseMapper.selectCarPageTotal(etpId,licCodeOrriverName,scheduleStatus);
    }
    /**
     * 保存车辆照片
     *
     * @param id
     * @param addr
     * @return
     */
    @Override
    public R saveCarPhoto(Integer id, String addr) {
        CarInfo carInfo = baseMapper.selectOne(new QueryWrapper<CarInfo>().eq("id", id));
        carInfo.setCarAddr(addr);
        baseMapper.updateById(carInfo);
        return R.ok();
    }

    /**
     * 获取企业的车辆数量
     *
     * @param etpId
     * @return
     */
    @Override
    public Integer getCarCount(Integer etpId) {
        Integer count = baseMapper.selectCount(new QueryWrapper<CarInfo>()
                .eq("etp_id", etpId).eq("del_flag", 0));
        return count;
    }

    /**
     * app端分页
     *
     * @param carInfoDTO
     * @return
     */
    @Override
    public R<Page<CarInfo>> appQueryPage(CarInfoDTO carInfoDTO) {
        Page<CarInfo> carInfoPage = this.queryPage(carInfoDTO);
        //分页数据
        List<CarInfo> records = carInfoPage.getRecords();
        //正在使用的车辆信息
        List<Integer> usingCarIds = carOrderService.getUsingCarIds();
        List<CarInfo> resultList = new ArrayList<>();
        if (CollUtil.isNotEmpty(usingCarIds) && CollUtil.isNotEmpty(records)) {
            for (CarInfo carInfo : records) {
                Integer id = carInfo.getId();
                //车辆正在使用
                if (usingCarIds.contains(id)) {
                    carInfo.setOrderStatus(0);
                }
                resultList.add(carInfo);
            }
            //添加车辆是否使用字段
          carInfoPage.setRecords(resultList);
        }
        return R.ok(carInfoPage);
    }

    /**
     * 解绑司机
     *
     * @param driverId
     * @return
     */
    @Override
    public R untieCarByDriverId(Integer driverId) {
        baseMapper.untieCarByDriverId(driverId);
        return R.ok();
    }


    /**
     * 获取企业id
     *
     * @param deviceDTO
     */
    private void getEtpId(CarInfoDTO deviceDTO) {
        OvmsUser user = SecurityUtils.getUser();
        if (deviceDTO.getEtpId() == null && user != null) {
            if (user.getEtpId() != CommonConstants.ETP_ID_1) {
                deviceDTO.setEtpId(user.getEtpId());
            }
        }
    }
    /**
     * 排班车辆信息不分页
     * @return
     */
    @Override
    public List<ApplyCarOrderAndDriverVO> selectCarList(CarDriverScheduleNoPageDTO carDriverScheduleNoPageDTO) {
        return baseMapper.selectCarList(carDriverScheduleNoPageDTO);
    }
}
