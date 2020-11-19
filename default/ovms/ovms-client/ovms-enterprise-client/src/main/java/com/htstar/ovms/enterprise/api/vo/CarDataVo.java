package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javafx.scene.chart.ValueAxis;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/16
 * Company: 航通星空
 * Modified By: 年检
 * @author liuwei
 */
@Data
@ApiModel("车辆数据概要")
public class CarDataVo implements Serializable {
    @ApiModelProperty(value = "车辆总数")
    private Integer carCount=0;
    @ApiModelProperty(value = "车辆在线数")
    private Integer online=0;
    @ApiModelProperty(value = "车辆离线数量")
    private Integer notOnline=0;
    @ApiModelProperty(value = "已绑定设备数")
    private Integer deviceSn=0;
    @ApiModelProperty(value = "未绑定设备数")
    private Integer noDeviceSn=0;
    @ApiModelProperty(value = "保险到期数")
    private Integer insurance=0;
    @ApiModelProperty(value = "保养到期数")
    private Integer maintenance=0;
    @ApiModelProperty(value = "年检到期数")
    private Integer mot=0;
}
