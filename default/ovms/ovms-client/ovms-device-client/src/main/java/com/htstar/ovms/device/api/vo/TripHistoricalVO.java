package com.htstar.ovms.device.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/6/2314:04
 */
@Data
public class TripHistoricalVO implements Serializable {

    @ApiModelProperty(value = "车辆id")
    private String carId;
    @ApiModelProperty(value = "设备编号")
    private String deviceSn;

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "公司名称")
    private String etpName;

    @ApiModelProperty(value = "子品牌名称")
    private String carSubBrand;

    @ApiModelProperty(value = "日里程")
    private String dayTimeLong;

    @ApiModelProperty(value = "月里程")
    private String monthTimeLong;

    @ApiModelProperty(value = "年里程")
    private String yearTimeLong;

}
