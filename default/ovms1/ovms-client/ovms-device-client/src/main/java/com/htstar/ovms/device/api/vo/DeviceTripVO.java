package com.htstar.ovms.device.api.vo;

import com.htstar.ovms.device.api.entity.DeviceTrip;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 行程表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Data
public class DeviceTripVO extends DeviceTrip {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "行程时间长度")
    private String timeLong;

    @ApiModelProperty(value = "车牌号")
    private String licCode;
}

