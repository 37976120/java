package com.htstar.ovms.device.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/2216:23
 */
@Data
public class ExportTripReportFormsDTO implements Serializable {

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "年份")
    private String year;

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "企业id")
    private Integer etpId;
}
