package com.htstar.ovms.enterprise.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 
 *  根据条件判断车辆是否能使用，推送违规消息
 * @author htxk
 * @date 2020-10-29 12:07:04
 */
@Data
public class CarSchedulingTimeWhereDTO  {
    @ApiModelProperty(value="排班日期")
    private String notScheduleWeek;

    /**
     * 开始时间
     */
    @ApiModelProperty(value="开始时间")
    private String statime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value="结束时间")
    private String endtime;

    @ApiModelProperty(value="车牌号")
    private String licCode;

}
