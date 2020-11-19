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
 * 设备最后一次GPS表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Data
@TableName("device_gps_last")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备最后一次GPS表")
public class DeviceGpsLast extends Model<DeviceGpsLast> {
    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    @TableId
    @ApiModelProperty(value = "设备编号")
    private String deviceSn;
    /**
     * GPS时间，标准时间
     */
    @ApiModelProperty(value = "GPS时间，标准时间")
    private LocalDateTime collectDatetime;
    /**
     * 接收时间，计算机本地时间
     */
    @ApiModelProperty(value = "接收时间，计算机本地时间")
    private LocalDateTime rcvTime;
    /**
     * 纬度 保留7位小数点（正负）
     */
    @ApiModelProperty(value = "纬度 保留7位小数点（正负）")
    private Double lat;
    /**
     * 经度 保留7位小数点（正负）
     */
    @ApiModelProperty(value = "经度 保留7位小数点（正负）")
    private Double lng;
    /**
     * 速度 公里/小时 保留2位小数点
     */
    @ApiModelProperty(value = "速度 公里/小时 保留2位小数点")
    private Float speed;
    /**
     * 方向 保留2位小数点
     */
    @ApiModelProperty(value = "方向 保留2位小数点")
    private Float direction;
    /**
     * 定位标志：0=未定位；1=2D定位；2=3D定位；
     */
    @ApiModelProperty(value = "定位标志：0=未定位；1=2D定位；2=3D定位；")
    private String gpsFlag;
    /**
     * 0:正常上传 1：补传
     */
    @ApiModelProperty(value = "0:正常上传 1：补传")
    private String flag;
    /**
     * ACC状态
     */
    @ApiModelProperty(value = "ACC状态")
    private String accStatus;
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
     * 累计里程 单位:米(M)
     */
    @ApiModelProperty(value = "累计里程 单位:米(M)")
    private Long totalTripMileage;
    /**
     * 当前的里程 单位:米(M)
     */
    @ApiModelProperty(value = "当前的里程 单位:米(M)")
    private Integer currentTripMileage;
    /**
     * 累计油耗 单位: 升(L)
     */
    @ApiModelProperty(value = "累计油耗 单位: 升(L)")
    private Double totalFuel;
    /**
     * 当前油耗 单位: 升(L)
     */
    @ApiModelProperty(value = "当前油耗 单位: 升(L)")
    private Double currentFuel;
    /**
     * 转速
     */
    @ApiModelProperty(value = "转速")
    private Integer rpm;
    /**
     * 0：东经；1：西经
     */
    @ApiModelProperty(value = "0：东经；1：西经")
    private Integer longitudeWay;
    /**
     * 0：北纬；1：南纬
     */
    @ApiModelProperty(value = "0：北纬；1：南纬")
    private Integer latitudeWay;
    /**
     * 数据状态：1表示已被逻辑删除，其它表示正常数据 默认为0
     */
    @ApiModelProperty(value = "数据状态：1表示已被逻辑删除，其它表示正常数据 默认为0")
    private Integer deleteFlag;
    /**
     * 0:不在线 1：在线
     */
    @ApiModelProperty(value = "0:不在线 1：在线")
    private Integer online;


}
