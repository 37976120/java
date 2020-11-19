package com.htstar.ovms.enterprise.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/6
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel(value = "退回请求参数")
public class WithdrawReq {
    @ApiModelProperty("ID")
    private Integer id;
    @ApiModelProperty("退回备注")
    private String remark;
}
