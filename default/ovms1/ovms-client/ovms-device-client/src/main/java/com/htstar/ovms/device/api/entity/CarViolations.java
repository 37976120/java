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
 * 
 *
 * @author htxk
 * @date 2020-10-29 18:39:11
 */
@Data
@TableName("car_violations")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class CarViolations extends Model<CarViolations> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;

    /**
     * 设备号
     */
    @ApiModelProperty(value="设备号")
    private String deviceSn;
    /**
     * 开始时间
     */
    @ApiModelProperty(value="开始时间")
    private LocalDateTime staTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value="结束时间")
    private LocalDateTime endTime;
    /**
     * 开始地址
     */
    @ApiModelProperty(value="开始地址")
    private String staAddr;
    /**
     * 结束地址
     */
    @ApiModelProperty(value="结束地址")
    private String endAddr;


}
