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
 * 设备最新状态
 *
 * @author 范利瑞
 * @date 2020-06-20 16:42:12
 */
@Data
@TableName("device_last_data")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备最新状态")
public class DeviceLastData extends Model<DeviceLastData> {
private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId
    @ApiModelProperty(value="自增ID")
    private Integer id;
    /**
     * 设备序列号
     */
    @ApiModelProperty(value="设备序列号")
    private String deviceSn;
    /**
     * 在线状态：0=不在线，1=在线；
     */
    @ApiModelProperty(value="在线状态：0=不在线，1=在线；")
    private Integer online;
    /**
     * 0:熄火 1：运行 2：怠速
     */
    @ApiModelProperty(value="0:熄火 1：运行 2：怠速")
    private Integer carStatus;
    /**
     * 设备上传：GPS时间，已转换为北京时间
     */
    @ApiModelProperty(value="设备上传：GPS时间，已转换为北京时间")
    private LocalDateTime gpsTime;
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
     * 速度
     */
    @ApiModelProperty(value="速度")
    private Float speed;
    /**
     * 方向 保留2位小数点
     */
    @ApiModelProperty(value="方向 保留2位小数点")
    private Float direction;
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
     * 定位标志：0=未定位；1=2D定位；2=3D定位；
     */
    @ApiModelProperty(value="定位标志：0=未定位；1=2D定位；2=3D定位；")
    private Integer gpsFlag;

    /**
     * 定位星数
     */
    @ApiModelProperty(value="定位星数")
    private Integer positionStarNum;
    /**
     * 最后一次打火时间
     */
    @ApiModelProperty(value="最后一次打火时间")
    private LocalDateTime lastAcconTime;
    /**
     * 累计里程，单位：M
     */
    @ApiModelProperty(value="累计里程，单位：M")
    private Double totalTripMileage;
    /**
     * 当前里程，单位:M
     */
    @ApiModelProperty(value="当前里程，单位:M")
    private Double currentTripMileage;
    /**
     * 当前油耗 单位: 升(L)
     */
    @ApiModelProperty(value="当前油耗 单位: 升(L)")
    private Double currentFuel;
    /**
     * 累计油耗 单位: 升(L)
     */
    @ApiModelProperty(value="累计油耗 单位: 升(L)")
    private Double totalFuel;
    /**
     * 转速
     */
    @ApiModelProperty(value="转速")
    private Integer rpm;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;


}
