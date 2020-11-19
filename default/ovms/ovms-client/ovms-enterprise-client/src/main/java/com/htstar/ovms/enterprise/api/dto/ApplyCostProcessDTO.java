package com.htstar.ovms.enterprise.api.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.lang.ref.PhantomReference;
import java.time.LocalDateTime;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/7
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel("费用审批参数")
public class ApplyCostProcessDTO {
    /**
     * 费用id
     */
    @ApiModelProperty(value="费用id")
    private Integer costId;
    /**
     * 车辆id
     */
    @ApiModelProperty(value="车辆id")
    private Integer carId;
    @ApiModelProperty(value="费用金额")
    private Integer cost;
    @ApiModelProperty(value="台账相关的费用类型 1:停车费 2:违章罚款 3:洗车美容 4:汽车用品 5:其他费用 6:通行费费用 7:加油费用 8:保险费用 9:保养费用 10:年检费用 11:维修费用 12:事故费用")
    private Integer costType;
    @ApiModelProperty(value="费用时间")
    private LocalDateTime costTime;

}
