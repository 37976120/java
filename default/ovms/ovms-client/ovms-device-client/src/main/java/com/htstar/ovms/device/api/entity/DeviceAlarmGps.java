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
 * 警情相关的GPS数据
 *
 * @author flr
 * @date 2020-06-19 17:23:59
 */
@Data
@TableName("device_alarm_gps")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "警情相关的GPS数据")
public class DeviceAlarmGps extends Model<DeviceAlarmGps> {
private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId
    @ApiModelProperty(value="自增ID")
    private Integer id;
    /**
     * 设备上传：GPS时间，已转换为北京时间
     */
    @ApiModelProperty(value="设备上传：GPS时间，已转换为北京时间")
    private LocalDateTime gpsTime;
    /**
     * 平台接收时间：北京时间
     */
    @ApiModelProperty(value="平台接收时间：北京时间")
    private LocalDateTime rcvTime;
    /**
     * 纬度 保留7位小数点（正负）
     */
    @ApiModelProperty(value="纬度 保留7位小数点（正负）")
    private Double lat;
    /**
     * 经度 保留7位小数点（正负）
     */
    @ApiModelProperty(value="经度 保留7位小数点（正负）")
    private Double lng;


    /**
     * GSP地址
     */
    @ApiModelProperty(value="GSP地址")
    private String gpsAddr;

    /**
     * 速度 公里/小时 保留2位小数点
     */
    @ApiModelProperty(value="速度 公里/小时 保留2位小数点")
    private Float speed;
    /**
     * 方向 保留2位小数点
     */
    @ApiModelProperty(value="方向 保留2位小数点")
    private Float direction;
    /**
     * 定位标志：0=未定位；1=2D定位；2=3D定位；
     */
    @ApiModelProperty(value="定位标志：0=未定位；1=2D定位；2=3D定位；")
    private Integer gpsFlag;
    /**
     * 0：东经；1：西经
     */
    @ApiModelProperty(value="0：东经；1：西经")
    private Integer longitudeWay;
    /**
     * 0：北纬；1：南纬
     */
    @ApiModelProperty(value="0：北纬；1：南纬")
    private Integer latitudeWay;
    /**
     * 定位星数
     */
    @ApiModelProperty(value="定位星数")
    private Integer positionStarNum;


}
