package com.htstar.ovms.device.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description: 统计 里程数
 * @date 2020/6/2317:33
 */
@Data
public class StatisticsMileageVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "获取的里程日期")
    private int getTime;

    @ApiModelProperty(value = "这天的里程数")
    private Integer mileage;


}
