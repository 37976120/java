package com.htstar.ovms.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/8/10
 * Company: 航通星空
 * Modified By:
 */
@Data
public class RespMonthReportVO {
    private List<MonthReportVO> monthReportList;

    /**
     *用车次数
     */
    @ApiModelProperty(value = "总计用车次数")
    private int useCarCount;
    /**
     * 公车用车次数
     */
    @ApiModelProperty(value = "总计公车用车次数")
    private int publicUseCarCount;
    /**
     * 私车公用次数
     */
    @ApiModelProperty(value = "总计私车公用次数")
    private int privateUseCarCount;
    /**
     * 司机出车次数
     */
    @ApiModelProperty(value = "总计司机出车次数")
    private int dirverWorkCount;
    /**
     * 自驾用车次数
     */
    @ApiModelProperty(value = "总计自驾用车次数")
    private int slfeDriveCount;
    /**
     * 车辆空置数
     */
    @ApiModelProperty(value = "总计车辆空置数")
    private int carEmptyCount;
    /**
     * 车辆空置率
     */
    @ApiModelProperty(value = "总计车辆空置率")
    private BigDecimal carEmptyRate = new BigDecimal("0.00");
}
