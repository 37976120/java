package com.htstar.ovms.device.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description: 统计油耗时间
 * @date 2020/6/2317:33
 */
@Data
public class StatisticsFuelConsumptionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "获取的油耗日期")
    private int getTime;

    @ApiModelProperty(value = "这天的油耗")
    private Integer fuelConsumption;


}
