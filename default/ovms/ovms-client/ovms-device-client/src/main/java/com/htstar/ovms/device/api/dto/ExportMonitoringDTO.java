package com.htstar.ovms.device.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/711:50
 */
@Data
public class ExportMonitoringDTO implements Serializable {
    @ApiModelProperty(value = "企业id")
    private Integer etpId;

    @ApiModelProperty(value = "车辆id")
    private Integer carId;

    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "车辆状态")
    private Integer online;
}
