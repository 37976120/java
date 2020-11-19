package com.htstar.ovms.device.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/6/2314:08
 */
@Data
public class TripHistoricalDTO extends Page {
    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "公司Id")
    private Integer etpId = null;

    @ApiModelProperty(value = "设备编号")
    private String deviceSn;

    @ApiModelProperty(value = "选择查看时间 1 本日 2 本月 3 本年")
    private Integer selectDate;

    @ApiModelProperty(value = "前一天或后一天时间")
    private String dayTime;

    @ApiModelProperty(value = "选择查看时间 1 本日 2 本月 3 本年  数组")
    private Integer[] selectDates;

}
