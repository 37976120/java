package com.htstar.ovms.device.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.DataOutput;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description: 监控列表
 * @date 2020/7/416:13
 */
@Data
@ApiModel(value = "监控列表")
public class MonitoringVO implements Serializable {
    @ApiModelProperty(value = "企业名称")
    private String etpName;
    @ApiModelProperty(value = "车辆id")
    private Integer carId;
    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value ="设备编号")
    private String deviceSn;

    @ApiModelProperty(value = "定位时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime gpsTime;

    @ApiModelProperty(value = "定位标志：0=未定位；1=2D定位；2=3D定位")
    private Integer gpsFlag;

    @ApiModelProperty(value = "速度")
    private Double speed;

    @ApiModelProperty(value = "方向数值")
    private Double direction;

    @ApiModelProperty(value = "方向文字")
    private String orientation;

    @ApiModelProperty(value = "纬度")
    private Double lat;

    @ApiModelProperty(value = "经度")
    private Double lng;

    @ApiModelProperty(value = "位置")
    private String addr;

    @ApiModelProperty(value = "0离线 1上线")
    private Integer online;

    @ApiModelProperty(value = "通信时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "省级区域编码")
    private String mapAreaCode1;

    @ApiModelProperty(value = "市级区域编码")
    private String mapAreaCode2;

    @ApiModelProperty(value = "区级区域编码")
    private String mapAreaCode3;
}
