package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/7
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ApplyCarProcessVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="id")
    private Integer id;
    @ApiModelProperty(value="企业id")
    private Integer etpId;
    @ApiModelProperty(value="流程类型:0=公车申请；1=私车公用；")
    private Integer processType;
    @ApiModelProperty(value="审批节点ID")
    private String verifyNodeList;
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value="流程列表有序")
    private List<ApplyVerifyNodeVO> applyVerifyNodeList;

    /**
     * 是否由分配司机提车：0=否，1=是；
     */
    @ApiModelProperty(value="是否由分配司机提车：0=否，1=是；")
    private Integer driverGetCarStatus;
    /**
     * 提车还车时需录入里程:0=否；1=是；
     */
    @ApiModelProperty(value="提车还车时需录入里程:0=否；1=是；")
    private Integer mileageStatus;
}
