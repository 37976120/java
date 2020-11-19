package com.htstar.ovms.device.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备
 *
 * @author flr
 * @date 2020-06-09 11:25:24
 */
@Data
@TableName("device")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备")
public class Device extends Model<Device> {
    private static final long serialVersionUID = 1L;

    /**
     * 内部ID
     */
    @TableId
    @ApiModelProperty(value = "内部ID")
    private Integer id;
    /**
     * 设备序列号
     */
    @ApiModelProperty(value = "设备序列号")
    @Excel(name = "设备序列号(*)", isImportField = "true")
    private String deviceSn;
    /**
     * 设备型号（字典）
     */
    @ApiModelProperty(value = "设备型号（字典）")
    private Integer productType;
    /**
     * 协议类型（字典）
     */
    @ApiModelProperty(value = "协议类型（字典）")
    @Excel(name = "协议类型", isImportField = "true")
    private Integer agreeType;

    /**
     * 固件版本号
     */
    @ApiModelProperty(value = "固件版本号")
    @Excel(name = "固件版本号", isImportField = "true")
    private Integer firmwareId;
    /**
     * 硬件版本号
     */
    @ApiModelProperty(value = "硬件版本号")
    @Excel(name = "硬件版本号", isImportField = "true")
    private Integer hardwareId;
    /**
     * 0,正常，1：停用
     */
    @ApiModelProperty(value = "0,正常，1：停用")
    private Integer deviceStatus;

    @ApiModelProperty(value = "企业ID")
    private Integer etpId;
    /**
     * 操作人（添加数据管理员）
     */
    @ApiModelProperty(value = "操作人（添加数据管理员）")
    private Integer userId;

    @TableField(exist = false)
    @ApiModelProperty(value = "sim卡号")
    private String sim;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime updateTime;

    /**
     * 设备价值
     */
    @ApiModelProperty(value = "设备价值")
    private String deviceValue;
    /**
     * 购置时期
     */
    @ApiModelProperty(value = "购置时期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime purchaseDate;
    /**
     * 作废日期
     */
    @ApiModelProperty(value = "作废日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime invalidDate;
    /**
     * 作废原因
     */
    @ApiModelProperty(value = "作废原因")
    private String invalidReason;
}
