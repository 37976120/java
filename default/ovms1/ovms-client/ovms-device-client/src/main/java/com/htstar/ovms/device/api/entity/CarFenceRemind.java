package com.htstar.ovms.device.api.entity;

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
 * 车辆围栏提醒记录
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
@Data
@TableName("car_fence_remind")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车辆围栏提醒记录")
public class CarFenceRemind extends Model<CarFenceRemind> {
private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId
    @ApiModelProperty(value="自增ID")
    private Integer id;
    /**
     * 车辆ID
     */
    @ApiModelProperty(value="车辆ID")
    private Integer carId;
    /**
     * 行程ID
     */
    @ApiModelProperty(value="行程id")
    private Integer tripId;
    /**
     * 提醒类型：0=不提醒；2=驶入提醒；3=驶出提醒；4=驶入驶出都提醒；
     */
    @ApiModelProperty(value="提醒类型：0=不提醒；2=驶入提醒；3=驶出提醒；4=驶入驶出都提醒；")
    private Integer remindType;
    /**
     * 提醒时间
     */
    @ApiModelProperty(value="提醒时间")
    private LocalDateTime createTime;


}
