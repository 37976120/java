package com.htstar.ovms.device.api.vo;

import com.htstar.ovms.device.api.entity.DeviceTrip;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 行程表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Data
public class DeviceTripTotalVO extends DeviceTrip {
    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "行程时间长度")
//    private String timeLong;

    @ApiModelProperty(value = "车牌号")
    private String licCode;
    @ApiModelProperty(value = "总时间")
    private  String totaltimeLong;

    @ApiModelProperty(value = "总里程（米）")
    private Long totalMileage;

//    @ApiModelProperty(value = "一天的总油耗")
//    private BigDecimal totalFuels;

    @ApiModelProperty(value = "历史行程")
    private List<DeviceTrip> deviceTrips;
}
