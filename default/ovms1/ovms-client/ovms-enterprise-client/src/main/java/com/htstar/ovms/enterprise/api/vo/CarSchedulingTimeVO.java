package com.htstar.ovms.enterprise.api.vo;

import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.enterprise.api.entity.CarSchedulingTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 12:07:04
 */
@ApiModel("排班管理可排班")
@Data
public class CarSchedulingTimeVO extends CarSchedulingTime {


    /**
     * 排班id
     */

    @ApiModelProperty(value="排班id")
    private Integer id;
    /**
     * 车辆id
     */
    @ApiModelProperty(value="车辆id")
    private String carId;
    /**
     * 排班日期
     */
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

    @ApiModelProperty(value="车辆集合")
    private String licCodes;

    private List<Map<String,Object>>licCodesList;
}
