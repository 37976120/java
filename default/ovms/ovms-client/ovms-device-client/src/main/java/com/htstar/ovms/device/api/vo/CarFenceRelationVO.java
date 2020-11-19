package com.htstar.ovms.device.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/711:08
 */
@Data
public class CarFenceRelationVO implements Serializable {

    @ApiModelProperty(value = "围栏和车辆绑定id")
    private Integer id;

    @ApiModelProperty(value = "车辆id")
    private Integer carId;

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "车型车系")
    private String carSubBrand;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "提醒类型")
    private Integer remindType;

    @ApiModelProperty(value = "绑定围栏数量")
    private Integer fenceNum;

    @ApiModelProperty(value = "围栏名称")
    private String fenceName;

    @ApiModelProperty(value="纬度")
    private Double lat;

    @ApiModelProperty(value="经度")
    private Double lng;

    @ApiModelProperty(value = "中心点地址")
    private String centerAddr;
}
