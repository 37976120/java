package com.htstar.ovms.device.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/2214:27
 */
@Data
public class TripReportFormsVO implements Serializable {

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "月初里程")
    private Integer startMonthMileage;

    @ApiModelProperty(value = "月末里程")
    private Integer endMonthMileage;

    @ApiModelProperty(value = "月行驶里程")
    private Integer monthMileage;

    @ApiModelProperty(value = "月初油耗（L）")
    private BigDecimal startFuelConsumption;

    @ApiModelProperty(value = "月末油耗（L）")
    private BigDecimal endFuelConsumption;

    @ApiModelProperty(value = "月用油量（L）")
    private BigDecimal monthFuelConsumption;

    @ApiModelProperty(value = "月油耗/百公里")
    private BigDecimal monthFuelOfKilometers;
}
