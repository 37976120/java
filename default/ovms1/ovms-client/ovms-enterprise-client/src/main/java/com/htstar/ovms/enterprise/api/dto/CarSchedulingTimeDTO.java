package com.htstar.ovms.enterprise.api.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 12:07:04
 */
@Data
public class CarSchedulingTimeDTO extends Page {

    @ApiModelProperty(value="排班日期")
    private String notScheduleWeek;
    /**
     * 排班名称
     */
    @ApiModelProperty(value="排班名称")
    private String settingname;
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
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updatetime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value="结束时间")
    private LocalDateTime createtime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value="车辆Id")
    private String carId;

    @ApiModelProperty(value="企业Id")
    private Integer etpId;

}
