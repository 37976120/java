package com.htstar.ovms.device.protoco;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: OBD 统计数据包 STAT_DATA
 * Author: flr
 * Date: Created in 2020/6/15
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ObdStatDataTp {

    /**
     * 最后一次点火时间
     */
    @ApiModelProperty(value = "最后一次点火时间")
    private LocalDateTime lastAcconTime;

    @ApiModelProperty(value = "设备当前时间，转化为北京时间")
    private LocalDateTime deviceTime;


    /**
     * 累计里程 单位:米(M)
     */
    @ApiModelProperty(value = "累计里程 单位:米(M)")
    private Long totalTripMileage;
    /**
     * 当前的里程 单位:米(M)
     */
    @ApiModelProperty(value = "当前的里程 单位:米(M)")
    private Integer currentTripMileage;
    /**
     * 累计油耗 单位: 升(L)
     */
    @ApiModelProperty(value = "累计油耗 单位: 升(L)")
    private Double totalFuel;
    /**
     * 当前油耗 单位: 升(L)
     */
    @ApiModelProperty(value = "当前油耗 单位: 升(L)")
    private Double currentFuel;


    @ApiModelProperty(value = "当前汽车状态")
    private ObdVStateTp obdVStateTp;

    /**
     * byte 数据指针位置
     */
    @JsonIgnore
    private int index;
}
