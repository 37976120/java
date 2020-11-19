package com.htstar.ovms.enterprise.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description: 车辆定位展示
 * @date 2020/6/2214:15
 */
@Data
@ApiModel(value = "车辆定位展示VO")
public class CarLocationVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备编号")
    private String deviceSn;

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "在线状态：0=不在线，1=在线")
    private Integer online;

    @ApiModelProperty(value = "0:熄火 1：运行 2：怠速")
    private Integer carStatus;

    @ApiModelProperty(value = "纬度")
    private Double lat;

    @ApiModelProperty(value = "经度")
    private Double lng;

    @ApiModelProperty(value = "GPS时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime gpsTime;

    @ApiModelProperty(value = "公司名称")
    private String etpName;

    @ApiModelProperty(value = "子品牌名称")
    private String carSubBrand;


}
