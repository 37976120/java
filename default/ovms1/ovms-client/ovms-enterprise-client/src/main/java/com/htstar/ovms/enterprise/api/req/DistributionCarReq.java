package com.htstar.ovms.enterprise.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description: 分配车辆请求
 * Author: flr
 * Date: Created in 2020/7/8
 * Company: 航通星空
 * Modified By:
 */
@Data
public class DistributionCarReq {

    @ApiModelProperty(value="公车申请事件id(主键，不自增)",required = true)
    private Integer orderId;

    @ApiModelProperty(value="车辆id",required = true)
    private Integer carId;
}
