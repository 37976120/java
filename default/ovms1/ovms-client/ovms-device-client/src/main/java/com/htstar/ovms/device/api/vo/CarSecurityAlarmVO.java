package com.htstar.ovms.device.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/6/2411:41
 */
@Data
public class CarSecurityAlarmVO implements Serializable {
    @ApiModelProperty(value = "设备编号")
    private String deviceSn;

    @ApiModelProperty(value = "车牌号")
    private String licCode;
    @ApiModelProperty(value = "车辆id")
    private Integer carId;
    @ApiModelProperty(value = "公司名称")
    private String etpName;

    @ApiModelProperty(value = "子品牌名称")
    private String carSubBrand;

    @ApiModelProperty(value = "未读报警")
    private Integer unRead;

    @ApiModelProperty(value = "累积报警")
    private Integer totalAlarm;

    @ApiModelProperty(value = "最近一次检测时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime lastDateTime;
}
