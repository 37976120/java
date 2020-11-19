package com.htstar.ovms.device.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/810:36
 */
@Data
public class LatAndLngVO implements Serializable {

    /**
     * 纬度 保留7位小数点（正负）
     */
    @ApiModelProperty(value = "纬度")
    private Double lat;
    /**
     * 经度 保留7位小数点（正负）
     */
    @ApiModelProperty(value = "经度")
    private Double lng;
}
