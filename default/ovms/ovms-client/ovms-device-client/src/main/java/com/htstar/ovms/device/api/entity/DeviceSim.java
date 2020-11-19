package com.htstar.ovms.device.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备SIM卡表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Data
@TableName("device_sim")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备SIM卡表")
public class DeviceSim extends Model<DeviceSim> {
    private static final long serialVersionUID = 1L;


    /**
     * 自增id
     */
    @TableId
    @ApiModelProperty(value = "自增id")
    private Integer id;

    /**
     * sim卡号
     */
    @ApiModelProperty(value = "sim卡号")
    @Excel(name = "sim卡号(*)",isImportField = "true")
    private String sim;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "设备序列号")
    @Excel(name = "设备序列号",isImportField = "true")
    private String deviceSn;

    /**
     * 启用时间
     */
    @ApiModelProperty(value = "启用时间")
    @Excel(name = "启用时间", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd HH:mm:ss", isImportField = "true")
    private LocalDateTime startTime;
    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    @Excel(name = "到期时间", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd HH:mm:ss", isImportField = "true")
    private LocalDateTime endTime;
    /**
     * 导入/创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "导入/创建时间")
    @Excel(name = "导入/创建时间", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd HH:mm:ss", isImportField = "true")
    private LocalDateTime createTime;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

}
