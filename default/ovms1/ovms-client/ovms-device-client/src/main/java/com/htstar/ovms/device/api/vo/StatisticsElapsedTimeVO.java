package com.htstar.ovms.device.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description: 统计 耗时时间
 * @date 2020/6/2317:33
 */
@Data
public class StatisticsElapsedTimeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "获取的耗时日期")
    private int getTime;

    @ApiModelProperty(value = "这天的耗时")
    private Integer longTime;


}
