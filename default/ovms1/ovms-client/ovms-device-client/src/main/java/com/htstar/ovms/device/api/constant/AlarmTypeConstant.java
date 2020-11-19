package com.htstar.ovms.device.api.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/16
 * Company: 航通星空
 * Modified By:
 * @author liuwei
 */
@ApiModel("监控数据统计")
public class AlarmTypeConstant {
    @ApiModelProperty("急加速")
    public static final  int ACCELERATE=4;
    @ApiModelProperty("急减速")
    public static final  int DECELERATE=5;
    @ApiModelProperty("急转弯")
    public static final  int TURN_AROUND=12;
    @ApiModelProperty("超速")
    public static final  int SPEEDING=1;
    @ApiModelProperty("拔出")
    public static final  int PULL_OUT=14;
    @ApiModelProperty("低电")
    public static final  int LOW_BATTERY=2;
    @ApiModelProperty("拖吊")
    public static final  int TOW=7;
    @ApiModelProperty("点火")
    public static final  int IGNITION=22;
    @ApiModelProperty("水温")
    public static final  int WATER_TEMP=3;
    @ApiModelProperty("故障")
    public static final  int MALFUNCTION=24;
    @ApiModelProperty("碰撞")
    public static final  int COLLISION=17;
    @ApiModelProperty("越界")
    public static final  int FENCE=29;

}
