package com.htstar.ovms.enterprise.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.vo.CarInfoVO;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.enums.CarType;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.common.security.annotation.Inner;
import com.htstar.ovms.enterprise.api.dto.*;
import com.htstar.ovms.enterprise.api.entity.CarAlarmRemind;
import com.htstar.ovms.enterprise.api.entity.CarInfo;
import com.htstar.ovms.enterprise.api.vo.CarRealTimeDrivingVO;
import com.htstar.ovms.enterprise.service.CarDeviceService;
import com.htstar.ovms.enterprise.service.CarInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.service.ApiListing;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * 公车表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@RestController
@RequestMapping("/carinfo")
@Api(value = "carinfo", tags = "公车管理")
@Slf4j
public class CarInfoController {
    @Autowired
    private  CarInfoService carInfoService;
    @Autowired
    private  CarDeviceService carDeviceService;

    /**
     * 分页查询
     *
     * @param carInfoDTO
     * @return
     * @Author HanGuji
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/page")
    public R<Page<CarInfo>> selectCarInfoPage(@RequestBody CarInfoDTO carInfoDTO) {
        return R.ok(carInfoService.selectCarInfoPage(carInfoDTO));
    }


/*
    *//**
     * 通过id查询
     * @param id id
     * @return R
     *//*
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(carInfoService.getById(id));
    }*/

    /**
     * 根据deptId查询车辆信息
     */
    @ApiOperation(value = "根据deptId查询车辆信息", notes = "根据deptId查询车辆信息")
    @GetMapping("/getCarInfoByDeptId/{deptId}")
    public R getCarInfoByDeptId(@PathVariable("deptId")Integer deptId){
        List<CarInfoVO> carInfoByDeptId = carInfoService.getCarInfoByDeptId(deptId);
        return R.ok(carInfoByDeptId);
    }

    /**
     * 新增
     * @param carInfo
     * @return R
     */
    @ApiOperation(value = "新增基本信息 ", notes = "新增公车信息 ")
    @SysLog("新增基本信息")
    @PostMapping("/baseInfo")
    public R saveBaseInfo(@RequestBody CarInfoMapAreaDTO carInfo)
    {
        return carInfoService.saveBaseInfo(carInfo);
    }

    @ApiOperation(value = "新增行驶证信息 ", notes = "新增行驶证信息 ")
    @SysLog("新增行驶证信息")
    @PostMapping("/licenseInfo")
    public R saveLicenseInfo(@RequestBody CarInfo carInfo) {

        return carInfoService.saveDrivingLicenseInfo(carInfo);
    }
    @ApiOperation(value = "修改基本信息", notes = "修改基本信息")
    @SysLog("修改基本信息")
    @PutMapping("/baseInfo")
    //@PreAuthorize("@pms.hasPermission('admin_caracciditem_edit')" )
    public R updateBaseInfo(@RequestBody CarInfoMapAreaDTO carInfo) {

        return carInfoService.updateBaseInfo(carInfo);
    }

    @ApiOperation(value = "修改行驶证信息 ", notes = "修改行驶证信息 ")
    @SysLog("修改行驶证信息")
    @PutMapping("/licenseInfo")
    public R updateLicenseInfo(@RequestBody CarInfo carInfo) {
        return carInfoService.updateDrivingLicenseInfo(carInfo);
    }
    @ApiOperation(value = "绑定设备 ", notes = "绑定设备 ")
    @SysLog("绑定设备")
    @GetMapping("/binding/{carId}/{deviceSn}")
    public R binding(@PathVariable("carId") Integer carId,@PathVariable("deviceSn") String deviceSn) {
        return carInfoService.bindingDeviceSn(carId,deviceSn);
    }

    @ApiOperation(value = "车辆管理分页 ", notes = "车辆管理分页 ")
    @PostMapping("/queryPage")
    public R<Page<CarInfo>> queryPage(@RequestBody CarInfoDTO carInfoDTO){
        return  R.ok(carInfoService.queryPage(carInfoDTO));
    }
    @ApiOperation(value = "app端车辆管理分页 ", notes = "app端车辆管理分页 ")
    @PostMapping("/appQueryPage")
    public R<Page<CarInfo>> appQueryPage(@RequestBody CarInfoDTO carInfoDTO){
        return  carInfoService.appQueryPage(carInfoDTO);
    }
    @ApiOperation(value = "app端保存车辆照片",notes = "app端保存车辆照片")
    @GetMapping("/savePhoto/{id}/{addr}")
    public R saveCarPhoto(@PathVariable("id") Integer id, @PathVariable("addr") String addr){
        return carInfoService.saveCarPhoto(id, addr);
    }

    @ApiOperation(value = "app端设置车辆安防信息提醒",notes = "app端设置车辆安防信息提醒")
    @PostMapping("/setAlarm")
    public R setAlarmRemind(@RequestBody  CarAlarmRemind carAlarmRemind){
        return carDeviceService.setAlarmRemind(carAlarmRemind);
    }

    @ApiOperation(value = "app端获取车辆安防信息提醒",notes = "app端获取车辆安防信息提醒")
    @GetMapping("/getAlarm/{carId}")
    public R getAlarmRemind(@PathVariable("carId")  Integer  carId){
        return carDeviceService.getAlarmRemind(carId);
    }

    @ApiOperation(value = "档案管理导出车辆", notes = "档案管理导出车辆")
    @PostMapping("/export")
    public void exportExcel(@RequestBody CarInfoDTO carInfoDTO) {
       carInfoService.export(carInfoDTO);
    }

    /**
     * 修改
     *
     * @param carInfo 公车表
     * @return R
     */
    @ApiOperation(value = "修改 ", notes = "修改 ")
    @SysLog("修改")
    @PutMapping
    //@PreAuthorize("@pms.hasPermission('enterprise_carinfo_edit')" )
    public R updateById(@RequestBody CarInfo carInfo) {
        return R.ok(carInfoService.updateCarInfo(carInfo));
    }

    /**
     * 通过id删除
     *
     * @param ids
     * @return R
     */
    @ApiOperation(value = "批量删除 ", notes = "批量删除")
    @SysLog("批量删除")
    @DeleteMapping("/{ids}")
    public R removeById(@PathVariable String  ids) {

        return carInfoService.updateByIds(ids);
    }


    /**
     * 后台 根据车辆Id查询详细信息
     *
     * @param id 车辆id
     * @return R
     * @Author HanGuji
     */
    @ApiOperation(value = "根据车辆Id查询详细信息", notes = "根据车辆Id查询详细信息")
    @GetMapping("selectCarInfo/{id}")
    public R selectCarInfoById(@PathVariable("id") Integer id) {
        if (id == null){
            return R.failed("id 不能为空");
        }
        return R.ok(carInfoService.selectCarInfoById(id));
    }

    /**
     * 后台 根据车辆ids批量删除该车（逻辑删除）
     *
     * @param ids 车辆ids
     * @return R
     * @Author HanGuji
     */
    @ApiOperation(value = "根据车辆ids批量删除", notes = "根据车辆ids批量删除")
    @SysLog("根据车辆ids批量删除")
    @DeleteMapping("/deleteCarInfoByIds/{ids}")
    public R deleteCarInfoByIds(@PathVariable("ids") String ids) {
        return carInfoService.updateByIds(ids);
    }

    /**
     * 导出车辆信息 后台PC
     *
     * @Author Hanguji
     */
    @ApiOperation(value = "导出车辆信息", notes = "导出车辆信息")
    @SysLog("导出车辆信息")
    @PostMapping("/exportCarInfo")
    public void exportCarInfo(@RequestBody(required = false) CarInfoExportDTO carInfoDTO, HttpServletResponse response) {
        List<CarInfo> carInfos = carInfoService.exportCarInfo(carInfoDTO);
        if (CollectionUtils.isEmpty(carInfos)) {
            return;
        }
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.renameSheet("车辆信息");
        ArrayList<Map<String, Object>> rows = new ArrayList<>();
        for (CarInfo carInfo : carInfos) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("车牌号", carInfo.getLicCode() == null ? "" : carInfo.getLicCode());
            row.put("车辆所属企业", carInfo.getEtpName() == null ? "" : carInfo.getEtpName());
            row.put("录入日期", carInfo.getCreateTime() == null ? "" : carInfo.getCreateTime());
            row.put("车辆类型", carInfo.getCarType() == null ? "" : CarType.handleMsg(carInfo.getCarType()));
            row.put("车辆状态", carInfo.getDelFlag() == null ? "" : carInfo.getDelFlag() == 0 ? "正常" : "禁用");
            row.put("绑定设备", carInfo.getDeviceSn() == null ? "" : carInfo.getDeviceSn());
            row.put("创建人", carInfo.getUsername() == null ? "" : carInfo.getUsername());
            row.put("绑定驾驶员", carInfo.getDriverName() == null ? "" : carInfo.getDriverName());
            rows.add(row);
        }
        writer.write(rows, true);
        ServletOutputStream out = null;
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    new String(URLEncoder.encode("车辆信息" + OvmDateUtil.getCstNowDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                            , "UTF-8").getBytes("UTF-8"), "ISO8859-1") + ".xlsx");
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("车辆信息导出失败", e);
        } finally {
            writer.close();
            IoUtil.close(out);
        }

    }

    /**
     * 车辆定位信息
     */
    @ApiOperation(value = "车辆定位信息", notes = "车辆定位信息")
    @PostMapping("/carLocationPage")
    public R selectCarInfoPage(@RequestBody  CarLocationNoPageDTO carLocationNoPageDTO) {

        return carInfoService.selectCarLocationPage(carLocationNoPageDTO);
    }
//    /**
//     * 车辆定位经纬度信息
//     */
//    @ApiOperation(value = "车辆定位经纬度信息", notes = "车辆定位经纬度信息")
//    @PostMapping("/carLocations")
//    public R selectCarLocations(@RequestBody CarLocationNoPageDTO carLocationDTO) {
//        return carInfoService.selectCarLocations(carLocationDTO);
//    }
        /**
         * 车辆实时驾驶信息
         */
    @ApiOperation(value = "车辆实时驾驶信息", notes = "车辆实时驾驶信息")
    @GetMapping("/selectCarDriving/{deviceSn}")
    public R selectCarDriving(@PathVariable(value = "deviceSn") String deviceSn) {
        CarRealTimeDrivingVO carRealTimeDrivingVO = carInfoService.selectCarDriving(deviceSn);
        if(carRealTimeDrivingVO != null)return  R.ok(carRealTimeDrivingVO);
        return R.failed("该设备的车辆找不到驾驶行程");
    }

    /**
     * 获取车辆的最大ID
     */
    @ApiOperation(value = "获取车辆的最大ID", notes = "获取车辆的最大ID")
    @GetMapping("/selectMaxId")
    public int selectMaxId() {
        QueryWrapper<CarInfo> wrapper =new QueryWrapper<>();
        wrapper.select("max(id) as id");
        return carInfoService.getOne(wrapper).getId();
    }


    @ApiOperation(value = "根据车辆查询是否申请",notes = "根据车辆查询是否申请")
    @GetMapping("/getsetting/{licCode}")
    public int getsetting(@PathVariable("licCode") String licCode){
        QueryWrapper<CarInfo> c = new QueryWrapper<>();
        c.lambda().eq(CarInfo::getLicCode,licCode);
        c.lambda().eq(CarInfo::getApplyStatus,1);
        int count = carInfoService.count(c);
        return count;
    }
//    @ApiOperation(value = "根据排班id查询车辆",notes = "根据排班id查询车辆")
//    @GetMapping("/getBysettingId/{settingId}")
//    public R getBysettingId(@PathVariable("settingId") Integer settingId){
//        return R.ok(carInfoService.getBysettingId(settingId));
//    }
    @ApiOperation(value = "查询可以申请的车辆",notes = "查询可以申请的车辆")
    @PostMapping("/getapply")
    public R getapply(@RequestBody  CarInfoDTO carInfo){
        return R.ok(carInfoService.getapply(carInfo));
    }

    @ApiOperation(value = "根据车辆id更新车辆申请状态",notes = "根据车辆id更新车辆申请状态")
    @GetMapping("/updateApplyStatus/{carId}")
    public R updateApplyStatus(@PathVariable("carId") Integer carId){
        QueryWrapper<CarInfo> c = new QueryWrapper<>();
        UpdateWrapper<CarInfo> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(CarInfo::getId,carId);
        c.lambda().eq(CarInfo::getId,carId);
        CarInfo one = carInfoService.getOne(c);
        if(one.getApplyStatus() == 0){
            wrapper.set("apply_status",1);
            return R.ok(carInfoService.update(wrapper));
        }else{
            wrapper.set("apply_status",0);
            return R.ok(carInfoService.update(wrapper));
        }

    }
    @ApiOperation(value = "查询与该排班未关联的车",notes = "查询与该排班未关联的车")
    @PostMapping("/getapplyStatis")
    public R getapplyStatis(@RequestBody CarSchedulingDTO carSchedulingDTO){
        return R.ok(carInfoService.getapplyStatis(carSchedulingDTO));
    }

}
