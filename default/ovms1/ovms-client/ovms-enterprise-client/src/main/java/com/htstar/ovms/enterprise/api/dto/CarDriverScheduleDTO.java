package com.htstar.ovms.enterprise.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/214:56
 */
@Data
@ApiModel(value = "车辆和司机排班查询条件")
public class CarDriverScheduleDTO extends Page {

    @ApiModelProperty(value = "查询类别 1 查询车辆信息 其他 司机信息")
    private Integer flag = null;

    @ApiModelProperty(value = "车牌号或者司机姓名")
    private String licCodeOrriverName;

    @ApiModelProperty(value = "查询时间")
    private String queryTime;

    @ApiModelProperty(value = "排班状态 1 已经有排班 0 未排班 在判断总数的时候 1 已经有排班 2 未排班")
    private Integer scheduleStatus = null;

    @ApiModelProperty(value = "车辆或司机状态 0 代表查询出来的正常的车和司机")
    private Integer carDeviceStatus = null;

    @ApiModelProperty(value = "企业id")
    private Integer etpId = null;

    @ApiModelProperty(value = "开始用车时间")
    private String startTime;

    @ApiModelProperty(value = "结束用车时间")
    private String endTime;

    @ApiModelProperty(value = "车辆排班总数")
    private long totals;

    @ApiModelProperty(value = "一到星期天每个车不可排班的星期")
    private Integer week;
}
