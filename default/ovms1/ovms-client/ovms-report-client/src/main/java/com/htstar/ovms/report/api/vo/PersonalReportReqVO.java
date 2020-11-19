package com.htstar.ovms.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PersonalReportReqVO {

    @ApiModelProperty(value = "人员名称")
    private String name;

    @ApiModelProperty(value = "车辆")
    private String carNo;

    @ApiModelProperty(value = "出车次数")
    private String useCount;

    @ApiModelProperty(value = "所属部门")
    private String deptName;

    @ApiModelProperty(value = "总计费用")
    private String costCount;


    @ApiModelProperty(value = "用车人ID")
    private Integer carUserId;


    @ApiModelProperty(value = "加油费用")
    private Integer fuelCost = 0;

    @ApiModelProperty(value = "通行费")
    private Integer etcCost = 0;

    @ApiModelProperty(value = "停车费")
    private Integer stopCost = 0;

    @ApiModelProperty(value = "洗车费")
    private Integer washCost = 0;

    @ApiModelProperty(value = "罚单费")
    private Integer ticketCost = 0;

    @ApiModelProperty(value = "其他费用")
    private Integer otherCost = 0;

    private Double costTotal = 0.00;
    private Integer useTotal = 0;
}
