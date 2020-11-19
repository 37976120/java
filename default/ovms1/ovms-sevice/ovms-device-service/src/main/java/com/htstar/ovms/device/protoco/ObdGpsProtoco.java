package com.htstar.ovms.device.protoco;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description: GPS协议数据
 * Author: flr
 * Date: Created in 2020/6/15
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ObdGpsProtoco implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String deviceSn;

    /**
     * 0:正常上传 1：补传
     */
    @ApiModelProperty(value = "0:正常上传 1：补传")
    private Integer flag;

    /**
     * 统计数据包
     * 其中UTC_Time为采集最后一条GPS数据的时间
     */
    private ObdStatDataTp obdStatDataTp;


    /**
     * GPS数据
     */
    private ObdGpsDataTp obdGpsDataTp;


    /**
     * RPM数据
     */
    private ObdRpmDataTp obdRpmDataTp;


    /**
     * 行程ID（自定义非协议内容）
     */
    private Integer tripId;
}
