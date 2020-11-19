package com.htstar.ovms.enterprise.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * @Author: lw
 * Date: Created in 2020/7/22
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel("充值请求参数")
public class RechargeReq {
    @ApiModelProperty("卡Id")
    private Integer Id;
    @ApiModelProperty("充值费用")
    private Integer cost;
    @ApiModelProperty("备注")
    private String remark;
}
