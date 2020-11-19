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

import java.time.LocalDateTime;

/**
 * 设备指令持久化
 *
 * @author flr
 * @date 2020-06-18 11:26:14
 */
@Data
@TableName("device_command")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备指令持久化")
public class DeviceCommand extends Model<DeviceCommand> {

    private static final long serialVersionUID = 1L;

    /**
     * ID 系统指令序列号（非下发到设备的序列号：用于检测网关发送情况）
     */
    @TableId
    @ApiModelProperty(value="ID")
    private Long id;
    /**
     * 设备序列号
     */
    @ApiModelProperty(value="设备序列号")
    private String deviceSn;
    /**
     * 操作用户ID非空
     */
    @ApiModelProperty(value="操作用户ID")
    private Integer userId;

    /**
     * 下发指令时协议包内的指令序列号
     */
    @ApiModelProperty(value="下发指令时协议包内的指令序列号")
    private Long protocoSeq;
    /**
     * 网关执行状态：0=未发送到网关；1=网关已收到；2=网关下发成功；
     */
    @ApiModelProperty(value="网关执行状态：0=未发送到网关；1=网关已收到；2=网关下发成功；3=网关执行失败")
    private Integer gatewayStatus;
    /**
     * 指令情况：0=未处理；1=未收到设备响应；2=执行成功；3=执行失败；4=取消下发；
     */
    @ApiModelProperty(value="指令情况：0=未处理；1=未收到设备响应；2=执行成功；3=执行失败；4=取消下发；")
    private Integer commandStatus;
    /**
     * 指令类型：1=及时下发；2=设备在线时下发；
     */
    @ApiModelProperty(value="指令类型：1=及时下发；2=设备在线时下发；")
    private Integer commandType;

    /**
     * 设置TLV数量
     */
    @ApiModelProperty(value="设置TLV数量")
    private Integer setNum;

    /**
     * 命令枚举（后续添加）：1=GPS设置
     * com.htstar.ovms.device.api.constant.CommandConstant
     */
    @ApiModelProperty(value="命令枚举（后续添加）：1=GPS设置")
    private Integer commandConstant;
    /**
     * 指令执行时间
     */
    @ApiModelProperty(value="指令执行时间")
    private LocalDateTime executeTime;
    /**
     * 指令下发允许尝试次数
     */
    @ApiModelProperty(value="指令下发允许尝试次数")
    private Integer allowTryNum;
    /**
     * 实际指令下发次数
     */
    @ApiModelProperty(value="实际指令下发次数")
    private Integer sendNum;
    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;


}
