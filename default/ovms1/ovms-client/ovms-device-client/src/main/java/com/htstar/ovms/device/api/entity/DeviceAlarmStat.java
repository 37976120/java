package com.htstar.ovms.device.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 警情相关的汽车状态
 *
 * @author flr
 * @date 2020-06-19 17:27:48
 */
@Data
@TableName("device_alarm_stat")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "警情相关的汽车状态")
public class DeviceAlarmStat extends Model<DeviceAlarmStat> {
private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId
    @ApiModelProperty(value="自增id")
    private Integer id;
    /**
     * 最后一次点火时间
     */
    @ApiModelProperty(value="最后一次点火时间")
    private LocalDateTime lastAcconTime;
    /**
     * 设备当前时间，转化为北京时间
     */
    @ApiModelProperty(value="设备当前时间，转化为北京时间")
    private LocalDateTime deviceTime;
    /**
     * 累计里程 单位:米(M)
     */
    @ApiModelProperty(value="累计里程 单位:米(M)")
    private Integer totalTripMileage;
    /**
     * 当前的里程 单位:米(M)
     */
    @ApiModelProperty(value="当前的里程 单位:米(M)")
    private Integer currentTripMileage;
    /**
     * 累计油耗 单位: 升(L)
     */
    @ApiModelProperty(value="累计油耗 单位: 升(L)")
    private Double totalFuel;
    /**
     * 当前油耗 单位: 升(L)
     */
    @ApiModelProperty(value="当前油耗 单位: 升(L)")
    private Double currentFuel;
    /**
     * 当前汽车状态（协议解析）
     */
    @ApiModelProperty(value="当前汽车状态")
    private String vstate;

    /**
     * 由s2中解析的点火状态：0=未点火，1=点火
     */
    @ApiModelProperty(value="当前汽车状态")
    private short fireState;


}
