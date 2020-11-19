package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * Description:
 * Date: Created in 2020/7/16
 * Company: 航通星空
 * Modified By:
 * @author lw
 */
@Data
@ApiModel("监控数据统计")
public class AlarmDataVo {
    @ApiModelProperty("急加速")
    private Integer accelerate=0;
    @ApiModelProperty("急减速")
    private Integer decelerate=0;
    @ApiModelProperty("急转弯")
    private Integer turnAround=0;
    @ApiModelProperty("超速")
    private Integer speeding=0;
    @ApiModelProperty("拔出")
    private Integer pullOut=0;
    @ApiModelProperty("低电")
    private Integer lowBattery=0;
    @ApiModelProperty("拖吊")
    private Integer tow=0;
    @ApiModelProperty("点火")
    private Integer ignition=0;
    @ApiModelProperty("水温")
    private Integer waterTemp=0;
    @ApiModelProperty("故障")
    private Integer malfunction=0;
    @ApiModelProperty("碰撞")
    private Integer collision=0;
    @ApiModelProperty("围栏")
    private Integer fence=0;
}
