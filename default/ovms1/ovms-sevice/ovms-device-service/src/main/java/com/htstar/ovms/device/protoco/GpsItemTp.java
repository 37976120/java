package com.htstar.ovms.device.protoco;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: GPS_ITEM
 * Author: flr
 * Date: Created in 2020/6/16
 * Company: 航通星空
 * Modified By:
 */
@Data
public class GpsItemTp {

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String deviceSn;

    /**
     * GPS时间，标准时间
     */
    @ApiModelProperty(value = "设备上传：GPS时间，已转换为北京时间")
    private LocalDateTime gpsTime;

    /**
     * 接收时间，计算机本地时间
     */
    @ApiModelProperty(value = "平台接收时间：北京时间")
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
    private Integer gpsFlag;
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
     * 定位星数
     */
    @ApiModelProperty(value = "定位星数")
    private Integer positionStarNum;
}
