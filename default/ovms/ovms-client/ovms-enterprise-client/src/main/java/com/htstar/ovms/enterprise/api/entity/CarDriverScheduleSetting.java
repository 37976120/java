package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设置车辆或者司机排班规则
 *
 * @author HanGuJi
 * @date 2020-07-01 15:03:18
 */
@Data
@TableName("car_driver_schedule_setting")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设置车辆或者司机排班规则")
public class CarDriverScheduleSetting extends Model<CarDriverScheduleSetting> {
private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty(value="主键")
    private Integer id;
    /**
     * 车辆id
     */
    @ApiModelProperty(value="车辆id")
    private Integer carId;
    /**
     * 司机id
     */
    @ApiModelProperty(value="司机id")
    private Integer driverId;
    /**
     * 排班状态 1 正常排班 0 永远不可排班
     */
    @ApiModelProperty(value="排班状态 1 正常排班 0 永远不可排班")
    private Integer scheduleStatus;
    /**
     * 重复不可排班日期 (1-7 代表 星期1 到星期天)多个 逗号分隔
     */
    @ApiModelProperty(value="重复不可排班日期 (1-7 代表 星期1 到星期天)多个 逗号分隔")
    private String notScheduleWeek;
    /**
     * 临时不可排班 开始时间
     */
    @ApiModelProperty(value="临时不可排班 开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime startTime;
    /**
     * 临时不可排班 结束时间
     */
    @ApiModelProperty(value="临时不可排班 结束时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "预约提示")
    private String remark;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;


}
