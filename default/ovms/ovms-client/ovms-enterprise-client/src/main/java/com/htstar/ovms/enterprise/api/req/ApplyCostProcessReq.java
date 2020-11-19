package com.htstar.ovms.enterprise.api.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/14
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel("费用审批查询数据")
public class ApplyCostProcessReq extends Page implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("审批人Id")
    private Integer operationUserId;
    @ApiModelProperty("审批配置Id")
    private Integer nodeId;
    @ApiModelProperty("申请人Id")
    private Integer applyUserId;
}
