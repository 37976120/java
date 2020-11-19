package com.htstar.ovms.device.api.vo;

import com.htstar.ovms.device.api.entity.Fence;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/710:22
 */
@Data
public class FencePageVO extends Fence {

    @ApiModelProperty(value = "绑定车辆数量")
    private Integer carNum;

    @ApiModelProperty(value = "中心点地址")
    private String centerAddr;

}
