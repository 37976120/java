package com.htstar.ovms.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:
 */
@Data
public class MonthReportVO {
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private String monthDate;
    /**
     *用车次数
     */
    @ApiModelProperty(value = "用车次数")
    private int useCarCount;
    /**
     * 公车用车次数
     */
    @ApiModelProperty(value = "公车用车次数")
    private int publicUseCarCount;
    /**
     * 私车公用次数
     */
    @ApiModelProperty(value = "私车公用次数")
    private int privateUseCarCount;
    /**
     * 司机出车次数
     */
    @ApiModelProperty(value = "司机出车次数")
    private int dirverWorkCount;
    /**
     * 自驾用车次数
     */
    @ApiModelProperty(value = "自驾用车次数")
    private int slfeDriveCount;
    /**
     * 车辆空置数
     */
    @ApiModelProperty(value = "车辆空置数")
    private int carEmptyCount;
    /**
     * 车辆空置率
     */
    @ApiModelProperty(value = "车辆空置率")
    private BigDecimal carEmptyRate = new BigDecimal("0.00");
}
