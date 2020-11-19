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
 * 设备警情
 *
 * @author flr
 * @date 2020-06-19 17:53:42
 */
@Data
@TableName("device_alarm")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备警情")
public class DeviceAlarm extends Model<DeviceAlarm> {
private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId
    @ApiModelProperty(value="自增id")
    private Integer id;
    /**
     * 设备序列号
     */
    @ApiModelProperty(value="设备序列号")
    private String deviceSn;

    /**
     * 终端设备自定义的警情编号，终端可以根据此编号确定中心平台的响应指令
     */
    @ApiModelProperty(value="终端设备自定义的警情编号，终端可以根据此编号确定中心平台的响应指令")
    private Long alarmNo;
    /**
     * 系统接收时间CST
     */
    @ApiModelProperty(value="系统接收时间CST")
    private LocalDateTime rcvTime;
    /**
     * 警情开始时间CST
     */
    @ApiModelProperty(value="警情开始时间CST")
    private LocalDateTime staTime;
    /**
     * 警情结束时间CST
     */
    @ApiModelProperty(value="警情结束时间CST")
    private LocalDateTime endTime;
    /**
     * 0=警情结束；1=新警情；
,持续警情警情（有结束标识0上来的）有：超速、低电压、水温告警、停车未熄火、转速高、尾气超标、疲劳驾驶、MIL故障告警；
及时警情（无结束标识0）：急加速、急减速、拖吊告警、上电告警、急变道、急转弯、危险驾驶、震动告警、断电告警、区域告警、紧急告警、碰撞告警、防拆告警、非法进入告警、非法点火告警、OBD剪线告警、点火告警、熄火告警
     */
    @ApiModelProperty(value="0=警情结束；1=新警情；")
    private Integer alarmFlag;
    /**
     * 告警类型:1:超速告警,2:低电压告警,3:水温告警,4:急加速告警,5:急减速告警,6:停车未熄火告警,7:拖吊告警,8:转速高告警,9:上电告警,10:尾气超标,11:急变道告警,12:急转弯告警,13:疲劳驾驶告警,14:断电告警,15:区域告警,16:紧急告警,17:碰撞告警,18:防拆告警,19:非法进入告警,20:非法点火告警,21:OBD剪线告警,22:点火告警,23:熄火告警,24:MIL故障告警,25:未锁车告警,26:未刷卡告警,27:危险驾驶告警,28:震动告警
     */
    @ApiModelProperty(value="告警类型:1:超速告警,2:低电压告警,3:水温告警,4:急加速告警,5:急减速告警,6:停车未熄火告警,7:拖吊告警,8:转速高告警,9:上电告警,10:尾气超标,11:急变道告警,12:急转弯告警,13:疲劳驾驶告警,14:断电告警,15:区域告警,16:紧急告警,17:碰撞告警,18:防拆告警,19:非法进入告警,20:非法点火告警,21:OBD剪线告警,22:点火告警,23:熄火告警,24:MIL故障告警,25:未锁车告警,26:未刷卡告警,27:危险驾驶告警,28:震动告警")
    private Integer alarmType;
    /**
     * 告警数据,目前只有:区域ID;或 超速阈值
     */
    @ApiModelProperty(value="告警数据,目前只有:区域ID;或 超速阈值")
    private String alarmDesc;

    /**
     * 警情相关的GPS信息
     */
    @ApiModelProperty(value="警情开始的GPS信息")
    private Integer staGpsId;
    /**
     * 警情相关的STAT汽车状态信息
     */
    @ApiModelProperty(value="警情开始的STAT汽车状态信息")
    private Integer staStatId;

    /**
     * 警情相关的GPS信息
     */
    @ApiModelProperty(value="警情结束的GPS信息")
    private Integer endGpsId;
    /**
     * 警情相关的STAT汽车状态信息
     */
    @ApiModelProperty(value="警情结束的STAT汽车状态信息")
    private Integer endStatId;

    /**
     * 警情是否已读 已读 1 未读 0
     */
    @ApiModelProperty(value = "已读 1 未读 0")
    private Integer isRead;
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

    @TableField(exist = false)
    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "逻辑删除 1 已删除 0 未删除 ")
    private Integer isDel;
    @TableField(exist = false)
    @ApiModelProperty(value="警情计数")
    private Integer alarmCount;

    @TableField(exist = false)
    @ApiModelProperty(value = "gps地址")
    private String gpsAddr;

}
