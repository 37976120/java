package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车辆安防提醒
 *
 * @author lw
 * @date 2020-08-10 10:18:08
 */
@Data
@TableName("car_alarm_remind")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车辆安防提醒")
public class CarAlarmRemind extends Model<CarAlarmRemind> {
private static final long serialVersionUID = 1L;

    /**
     * 车辆id
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="车辆id")
    private Integer carId;
    /**
     * 拔出
     */
    @ApiModelProperty(value="拔出")
    private Integer pullOut;
    /**
     * 低电
     */
    @ApiModelProperty(value="低电")
    private Integer lowBattery;
    /**
     * 拖吊
     */
    @ApiModelProperty(value="拖吊")
    private Integer tow;
    /**
     * 点火
     */
    @ApiModelProperty(value="点火")
    private Integer ignition;
    /**
     * 水温
     */
    @ApiModelProperty(value="水温")
    private Integer waterTemp;
    /**
     * 异常
     */
    @ApiModelProperty(value="异常")
    private Integer abnormal;
    /**
     * 故障
     */
    @ApiModelProperty(value="故障")
    private Integer malfunction;
    /**
     * 碰撞
     */
    @ApiModelProperty(value="碰撞")
    private Integer collision;
    /**
     * 超速类型
     */
    @ApiModelProperty(value="超速类型")
    private Integer speedType;
    /**
     * 行程报告
     */
    @ApiModelProperty(value="行程报告")
    private Integer report;
    /**
     * 周行程报告
     */
    @ApiModelProperty(value="周行程报告")
    private Integer weekReport;
    /**
     * 月行程报告
     */
    @ApiModelProperty(value="月行程报告")
    private Integer monthReport;
    /**
     * 限行报告
     */
    @ApiModelProperty(value="限行报告")
    private Integer resReport;


}
