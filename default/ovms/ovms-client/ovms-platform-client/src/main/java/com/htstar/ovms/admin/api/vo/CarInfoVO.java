package com.htstar.ovms.admin.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/411:45
 */
@Data
public class CarInfoVO implements Serializable {
    @ApiModelProperty(value = "车辆id")
    private Integer carId;

    @ApiModelProperty(value = "车牌号")
    private String licCode;
}
