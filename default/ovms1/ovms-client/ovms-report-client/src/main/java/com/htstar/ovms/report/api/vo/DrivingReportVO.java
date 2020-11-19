package com.htstar.ovms.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description:
 * Author: JinZhu
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:
 */
@Data
public class DrivingReportVO {

    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private String monthDate;
    /**
     * 每月行驶总行驶公里
     */
    @ApiModelProperty(value="每月行驶总行驶公里")
    private int drivingKmCount;
    /**
     * 每月行驶总时间
     */
    @ApiModelProperty(value="每月行驶总时间")
    private int drivingTimeCount;
    /**
     * 每月行驶总行驶油量
     */
    @ApiModelProperty(value="每月行驶总行驶油量")
    private int drivingOilTotal;
    /**
     * 每月行驶总百公里耗油量
     */
    @ApiModelProperty(value="每月行驶总百公里耗油量")
    private int drivingHkmOilTotal;
}
