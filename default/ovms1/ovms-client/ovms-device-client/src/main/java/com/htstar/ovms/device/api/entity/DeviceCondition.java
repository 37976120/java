package com.htstar.ovms.device.api.entity;

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
 * 设备工况检测
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Data
@TableName("device_condition")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备工况检测")
public class DeviceCondition extends Model<DeviceCondition> {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    @ApiModelProperty(value = "")
    private String deviceSn;
    /**
     * 接收时间，计算机本地时间
     */
    @ApiModelProperty(value = "接收时间，计算机本地时间")
    private LocalDateTime rcvTime;
    /**
     * 最后一次点火时间
     */
    @ApiModelProperty(value = "最后一次点火时间")
    private LocalDateTime lastAcconTime;
    /**
     * 设备当前时间
     */
    @ApiModelProperty(value = "设备当前时间")
    private LocalDateTime deviceUtcTime;
    /**
     * 工况个数
     */
    @ApiModelProperty(value = "工况个数")
    private Integer conCount;
    /**
     * 工况KEY
     */
    @ApiModelProperty(value = "工况KEY")
    private String conKey;
    /**
     * 工况KEY中分出来的类型
     */
    @ApiModelProperty(value = "工况KEY中分出来的类型")
    private Integer conType;
    /**
     * 工况数据
     */
    @ApiModelProperty(value = "工况数据")
    private String conValue;
    /**
     * 工况包序号
     */
    @ApiModelProperty(value = "工况包序号")
    private Integer packIndex;


}
