package com.htstar.ovms.device.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.ws.soap.Addressing;
import java.io.Serializable;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/610:01
 */
@Data
public class TripInfoDTO implements Serializable {
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "企业id")
    private Integer etpId;

}
