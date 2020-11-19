package com.htstar.ovms.device.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/910:34
 */
@Data
public class TripPlaybackVO implements Serializable {

    @ApiModelProperty(value = "行程开始时GPS点，格式：纬度,经度")
    private String staLatlon;

    @ApiModelProperty(value = "行程结束时GPS点，格式：纬度,经度")
    private String endLatlon;

    @ApiModelProperty(value = "中间点的经纬度")
    private List<LatAndLngVO> latAndLngVOList;
}
