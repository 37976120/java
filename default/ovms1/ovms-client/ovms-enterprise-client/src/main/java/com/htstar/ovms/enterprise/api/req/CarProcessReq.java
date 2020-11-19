package com.htstar.ovms.enterprise.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 用车申请流程配置Req
 * Author: flr
 * Date: Created in 2020/7/1
 * Company: 航通星空
 * Modified By:
 */
@Data
public class CarProcessReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 流程类型:0=公车申请；1=私车公用；
     */
    @ApiModelProperty(value="流程类型:0=公车申请；1=私车公用；",required = true)
    private Integer processType;

    @ApiModelProperty(value="节点列表", required = true)
    private List<VerifyNodeReq> verifyNodeList;

    /**
     * 是否由分配司机提车：0=否，1=是；
     */
    @ApiModelProperty(value="是否由分配司机提车：0=否，1=是；")
    private Integer driverGetCarStatus;
    /**
     * 提车还车时需录入里程:0=否；1=是；(自动获取)
     */
    @ApiModelProperty(value="提车还车时需录入里程:0=否；1=是；")
    private Integer mileageStatus;

}
