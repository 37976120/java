package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;

import com.htstar.ovms.common.security.annotation.Inner;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.dto.CarSchedulingTimeDTO;
import com.htstar.ovms.enterprise.api.dto.CarSchedulingTimeWhereDTO;
import com.htstar.ovms.enterprise.api.entity.CarInfo;
import com.htstar.ovms.enterprise.api.entity.CarSchedulingTime;
import com.htstar.ovms.enterprise.service.CarInfoService;
import com.htstar.ovms.enterprise.service.CarSchedulingTimeService;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 12:07:04
 */
@RestController
@AllArgsConstructor
@RequestMapping("/carschedulingtime" )
@Api(value = "carschedulingtime", tags = "管理排班")
public class CarSchedulingTimeController {

    private final CarSchedulingTimeService carSchedulingTimeService;

    private final CarInfoService carInfoService;
    /**
     * 分页查询
     * @param page 分页对象
     * @param carSchedulingTime 
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/page" )
    public R getCarSchedulingTimePage(@RequestBody  CarSchedulingTimeDTO carSchedulingTimeDTO) {

        return R.ok(carSchedulingTimeService.getSchedulingAll(carSchedulingTimeDTO));
    }


    /**
     * 通过日期查询判断是否使用车
     * @param
     * @return R
     */
    @ApiOperation(value = "通过日期查询判断是否使用车", notes = "通过日期查询判断是否使用车")
    @PostMapping("/rqweek")
    @Inner
    public int getCount(CarSchedulingTimeWhereDTO carSchedulingTimeWhereDTO,@RequestHeader(SecurityConstants.FROM) String from) {
        return carSchedulingTimeService.getBylicCode(carSchedulingTimeWhereDTO);
    }
    /**
     * 更具日期时间判断是否可以使用车，排班新车
     * @return
     */
    @GetMapping("/xcrqweek/{licCode}" )
    @Inner
    int getlicCodeCount(@PathVariable("licCode") String licCode,@RequestHeader(SecurityConstants.FROM) String from){
        return carSchedulingTimeService.getBylicCodeCount(licCode);
    }
    /**
     * 新增
     * @param carSchedulingTime 
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping
    public R save(@RequestBody CarSchedulingTime carSchedulingTime) {
        carSchedulingTime.setCreatetime(LocalDateTime.now());
        OvmsUser user = SecurityUtils.getUser();
        if ( user != null) {
            carSchedulingTime.setEtpId(user.getEtpId());
        }
        boolean save = carSchedulingTimeService.save(carSchedulingTime);
        if(save){
            return R.ok("保存成功");
        }else{
            return R.ok("保存失败");
        }

    }

    /**
     * 修改
     * @param carSchedulingTime 
     * @return R
     */
    @ApiOperation(value = "修改", notes = "修改")
    @PutMapping
    public R updateById(@RequestBody CarSchedulingTime carSchedulingTime) {
        carSchedulingTime.setUpdatetime(LocalDateTime.now());
        return R.ok(carSchedulingTimeService.updateById(carSchedulingTime));
    }

    /**
     * 通过id删除
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(carSchedulingTimeService.removeById(id));
    }

    /**
     * 通过id查詢
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查詢", notes = "通过id查詢")
    @SysLog("通过id查詢" )
    @GetMapping ("/{id}" )
    public R getByById(@PathVariable Integer id) {
        return R.ok(carSchedulingTimeService.getById(id));
    }

    /**
     * 修改
     * @param carSchedulingTime
     * @return R
     */
    @ApiOperation(value = "根据排班id删除车辆", notes = "根据排班id删除车辆")
    @PutMapping("/removeById")
    public R removeById(@RequestBody CarSchedulingTime carSchedulingTime) {
        carSchedulingTime.setUpdatetime(LocalDateTime.now());
        UpdateWrapper<CarSchedulingTime> c = new UpdateWrapper<>();
        c.lambda()
                    .set(CarSchedulingTime::getCarId,carSchedulingTime.getCarId())
                .or()
                .set(CarSchedulingTime::getUpdatetime,carSchedulingTime.getUpdatetime())
                .or()
                .eq(CarSchedulingTime::getId,carSchedulingTime.getId());
        return R.ok(carSchedulingTimeService.update(c));
    }
    @GetMapping("/getsettings/{licCode}")
    @Inner
    public int getsettings(@PathVariable("licCode") String licCode,@RequestHeader(SecurityConstants.FROM) String from){
        QueryWrapper<CarInfo> c = new QueryWrapper<>();
        c.lambda().eq(CarInfo::getLicCode,licCode);
        c.lambda().eq(CarInfo::getApplyStatus,1);
        int count = carInfoService.count(c);
        return count;
    }
}
