package com.htstar.ovms.device.api.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 行程表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Data
public class DeviceTripsVO extends Model<DeviceTripsVO> {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId
    @ApiModelProperty(value = "自增id")
    private Integer id;
    /**
     *设备序列号
     */
    @ApiModelProperty(value = "设备序列号")
    private String deviceSn;
    /**
     *行程开始时间
     */
    @ApiModelProperty(value = "行程开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime staTime;
    /**
     *行程结束时间
     */
    @ApiModelProperty(value = "行程结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime endTime;
    /**
     * 里程（米）
     */
    @ApiModelProperty(value = "里程（米）")
    private Integer mileage;
    /**
     * 油耗
     */
    @ApiModelProperty(value = "油耗")
    private BigDecimal fuelConsumption;
    /**
     * 总里程（米）
     */
    @ApiModelProperty(value = "总里程（米）")
    private Long totalMileage;
    /**
     * 总油耗
     */
    @ApiModelProperty(value = "总油耗")
    private BigDecimal totalFuel;
    /**
     * 行程开始时GPS点，格式：纬度,精度
     */
    @ApiModelProperty(value = "行程开始时GPS点，格式：纬度,精度")
    private String staLatlon;
    /**
     * 行程结束时GPS点，格式：纬度,精度
     */
    @ApiModelProperty(value = "行程结束时GPS点，格式：纬度,精度")
    private String endLatlon;
    /**
     * 行程开始位置
     */
    @ApiModelProperty(value = "行程开始位置")
    private String staAddr;
    /**
     * 行程结束位置
     */
    @ApiModelProperty(value = "行程结束位置")
    private String endAddr;
    /**
     * 统计服务使用(1:默认值 未统计 0：该车辆已经统计)
     */
    @ApiModelProperty(value = "统计服务使用(1:默认值 未统计 0：该车辆已经统计)")
    private Integer statisticsStatus;
    /**
     * 地址解析使用的更新标志(1:默认值 未解析 0：已解析)
     */
    @ApiModelProperty(value = "地址解析使用的更新标志(1:默认值 未解析 0：已解析)")
    private Integer upStatus;

    @ApiModelProperty(value = "一次行程急加速的次数")
    private Integer sharpAccelerate;

    @ApiModelProperty(value = "一次行程急减速的次数")
    private Integer sharpSlowdown;

    @ApiModelProperty(value = "一次行程急转弯的次数")
    private Integer sharpSwerve;

    @ApiModelProperty(value = "一次行程超速的次数")
    private Integer overSpeed;


    @ApiModelProperty(value = "行程时间长度")
    private String timeLong;

}
