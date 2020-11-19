package com.htstar.ovms.device.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description: 设备查询表
 * @date 2020/6/1512:04
 */
@Data
public class DeviceAlarmVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "GPS时间，标准时间，警情开始时间")
    private LocalDateTime collectDatetime;

    @ApiModelProperty(value = "GPS定位地址信息")
    private String gpsAddress;

    @ApiModelProperty(value = "警情类型")
    private Integer type;
}
