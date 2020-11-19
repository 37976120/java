package com.htstar.ovms.device.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/6/2412:04
 */
@Data
public class CarSecurityAlarmDTO extends Page {
    @ApiModelProperty(value = "车牌号")
    private String licCode;
    @ApiModelProperty(value = "车辆id")
    private String carId;

    @ApiModelProperty(value = "公司Id")
    private Integer etpId = null;

    @ApiModelProperty(value = "设备编号")
    private String deviceSn;

    @ApiModelProperty(value = "警报列表查询 开始时间")
    private String startTime;

    @ApiModelProperty(value = "警报列表查询 结束时间")
    private String endTime;

    @ApiModelProperty(value = "警报类型")
    private Integer alarmType = null;

    @ApiModelProperty(value="警报id")
    private String id;
}
