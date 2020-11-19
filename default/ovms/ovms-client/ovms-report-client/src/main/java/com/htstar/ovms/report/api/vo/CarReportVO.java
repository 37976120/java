package com.htstar.ovms.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/8/4
 * Company: 航通星空
 * Modified By:
 */
@Data
public class CarReportVO {

    @ApiModelProperty(value="车牌号")
    private String licCode;

    @ApiModelProperty(value="出车次数")
    private int useCount;

    @ApiModelProperty(value="所属部门")
    private String deptName;

    @ApiModelProperty(value="总计费用（元）")
    private String costCount = "0.00";
}
