package com.htstar.ovms.device.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HanGuJi
 * @Description: 获取行程轨迹详细GPS数据请求参数
 * @date 2020/7/911:48
 */
@Data
public class TripGpsDTO implements Serializable {

    @ApiModelProperty(value = "设备编号")
    private String deviceSn;

    @ApiModelProperty(value = "行程开始时GPS点，格式：纬度,经度")
    private String staLatlon;

    @ApiModelProperty(value = "行程结束时GPS点，格式：纬度,经度")
    private String endLatlon;

    @ApiModelProperty(value = "行程开始时间")
    private String startTime;

    @ApiModelProperty(value = "行程结束时间")
    private String endTime;
}
