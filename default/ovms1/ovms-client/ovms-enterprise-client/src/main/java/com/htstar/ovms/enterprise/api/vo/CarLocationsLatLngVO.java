package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author HanGuJi
 * @Description: 车辆定位展示
 * @date 2020/6/2214:15
 */
@Data
@ApiModel(value = "车辆定位展示VO")
public class CarLocationsLatLngVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "设备编号")
    private String deviceSn;

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "纬度")
    private int lat;

    @ApiModelProperty(value = "经度")
    private int lng;

    @ApiModelProperty(value = "在线状态：0=不在线，1=在线")
    private Integer online;

}
