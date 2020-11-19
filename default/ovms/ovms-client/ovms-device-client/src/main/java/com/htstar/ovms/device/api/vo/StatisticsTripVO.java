package com.htstar.ovms.device.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/6/2317:14
 */
@Data
public class StatisticsTripVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总的里程")
    private Integer totoalMileage;

    @ApiModelProperty(value = "日平均里程")
    private Double avgMileage;

    @ApiModelProperty(value = "每天的里程数据")
    private List<StatisticsMileageVO> statisticsMileageVOS;

    @ApiModelProperty(value = "总的耗时时间")
    private Integer totoalLongTIme;

    @ApiModelProperty(value = "日平均耗时")
    private Double avgLongTime;

    @ApiModelProperty(value = "每天的耗时数据")
    private List<StatisticsElapsedTimeVO> statisticsElapsedTimeVOS;

    @ApiModelProperty(value = "总的油耗")
    private Integer totoalFuelConsumption;

    @ApiModelProperty(value = "日平均油耗")
    private Double avgFuelConsumption;

    @ApiModelProperty(value = "每天的油耗数据")
    private List<StatisticsFuelConsumptionVO> statisticsFuelConsumptionVOS;
}
