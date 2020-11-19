package com.htstar.ovms.enterprise.controller;

import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.vo.AlarmDataVo;
import com.htstar.ovms.enterprise.api.vo.ApplyCarDataVo;
import com.htstar.ovms.enterprise.api.vo.CarDataVo;
import com.htstar.ovms.enterprise.service.CarDataAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Date: Created in 2020/7/16
 * Company: 航通星空
 * Modified By:
 *
 * @author liuwei
 */
@RestController
@RequestMapping("/carDataAnalysis")
@Api(value = "carDataAnalysis", tags = "首页数据")
@AllArgsConstructor
@Slf4j
public class CarDataAnalysisController {
    @Autowired
    private CarDataAnalysisService carDataAnalysisService;
    @ApiOperation(value = "车辆数据概要", notes = "车辆数据概要")
    @GetMapping("/carData")
    public R<CarDataVo> getCarDataByEtp() {
        return R.ok(carDataAnalysisService.getCarDataByEtp());
    }

    @ApiOperation(value = "用车数据总览", notes = "用车数据总览")
    @GetMapping("/applyData/{timeType}")
    public R<ApplyCarDataVo> getApplyCarData(@PathVariable("timeType") Integer timeType){
        return R.ok(carDataAnalysisService.getApplyCarData(timeType));
    }
    @ApiOperation(value = "企业警情数据", notes = "企业警情数据")
    @GetMapping("/getAlarmData/{timeType}")
    public R<AlarmDataVo> getAlarmData(@PathVariable("timeType") Integer timeType){
        return carDataAnalysisService.getAlarmData(timeType);
    }
}
