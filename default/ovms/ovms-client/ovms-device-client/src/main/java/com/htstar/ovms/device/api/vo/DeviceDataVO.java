package com.htstar.ovms.device.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description: 设备查询表
 * @date 2020/6/1512:04
 */
@Data
public class DeviceDataVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 内部ID
     */
    @ApiModelProperty(value = "内部ID")
    private Integer id;
    /**
     * 设备序列号
     */
    @ApiModelProperty(value = "设备序列号")
    private String deviceSn;
    /**
     * 设备型号（字典）
     */
    @ApiModelProperty(value = "设备型号（字典）")
    private Integer productType;

    @ApiModelProperty(value = "设备型号")
    private String lable;

    @ApiModelProperty(value = "企业id")
    private Integer etpId;

    @ApiModelProperty(value = "企业名称")
    private String etpName;

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "车架号")
    private String frameCode;

    @ApiModelProperty(value = "车型")
    private String carType;

    @ApiModelProperty(value = "绑定账号")
    private String username;

    @ApiModelProperty(value = "绑定时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "SIM卡号")
    private String sim;


}
