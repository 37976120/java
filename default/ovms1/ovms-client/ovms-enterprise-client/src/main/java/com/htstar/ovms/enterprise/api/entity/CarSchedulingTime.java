package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 *
 * @author htxk
 * @date 2020-10-29 12:07:04
 */
@Data
@TableName("car_scheduling_time")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class CarSchedulingTime extends Model<CarSchedulingTime> {
private static final long serialVersionUID = 1L;

    /**
     * 排班id
     */
    @TableId
    @ApiModelProperty(value="排班id")
    private Integer id;

    @ApiModelProperty(value="企业id")
    private Integer etpId;
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
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updatetime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value="结束时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createtime;


}
