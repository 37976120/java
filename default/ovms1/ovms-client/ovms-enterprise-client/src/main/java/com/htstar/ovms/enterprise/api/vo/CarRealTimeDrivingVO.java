package com.htstar.ovms.enterprise.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description: 实时驾驶
 * @date 2020/6/2215:06
 */
@Data
public class CarRealTimeDrivingVO implements Serializable {

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "用时，单位分钟")
    private Integer longTime;

    @ApiModelProperty(value = "里程")
    private Integer mileage;

    @ApiModelProperty(value = "油耗")
    private BigDecimal fuelConsumption;

    @ApiModelProperty(value = "转速")
    private Integer rpm;

    @ApiModelProperty(value = "速度")
    private Float speed;

    @ApiModelProperty(value = "开始经纬度")
    private String staLatlon;

    @ApiModelProperty(value = "结束经纬度")
    private String endLatlon;

    @ApiModelProperty(value = "行程时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "在线状态：0=不在线，1=在线")
    private Integer online;

    @ApiModelProperty(value = "低电报警")
    private String lowPoWerFlag;

    @ApiModelProperty(value = "水温警报")
    private String wtRFlag;
}
