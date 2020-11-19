package com.htstar.ovms.device.api.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.device.api.entity.DeviceTrip;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 行程表
 *
 * @author wj
 * @date 2020-06-12 11:38:07
 */
@Data
public class DeviceTripCountTotalVO {
    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "行程时间长度")
//    private String timeLong;

    @ApiModelProperty(value = "总时间")
    private  String totaltimeLong;

    @ApiModelProperty(value = "总里程（米）")
    private Long totalMileage;

    @ApiModelProperty(value = "一天的总油耗")
    private BigDecimal totalFuels;

    @ApiModelProperty(value = "历史行程")
    private List<DeviceTripVO> deviceTrips;

    @ApiModelProperty(value = "历史行程总数")
    private long total;

    @ApiModelProperty(value = "当前页")
    private long current;

    @ApiModelProperty(value = "每页显示")
    private long size;
}
