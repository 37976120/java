package com.htstar.ovms.enterprise.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/11
 * Company: 航通星空
 * Modified By:
 */
@Data
public class SetNodeVerifyUserReq {
    @ApiModelProperty(value="节点ID")
    private Integer nodeId;

    @ApiModelProperty(value="[审批人员ID列表(有序)]")
    private String verifyUserList;
}
