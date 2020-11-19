package com.htstar.ovms.device.api.vo;

import com.htstar.ovms.device.api.entity.DeviceTrip;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/610:33
 */
@Data
public class TripAndCarInfoVO extends DeviceTrip {

    @ApiModelProperty(value = "车牌号")
    private String licCode;

}
