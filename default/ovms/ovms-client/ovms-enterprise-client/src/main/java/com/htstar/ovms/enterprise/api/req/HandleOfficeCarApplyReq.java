package com.htstar.ovms.enterprise.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description: 处理公车申请
 * Author: flr
 * Date: Created in 2020/7/2
 * Company: 航通星空
 * Modified By:
 */
@Data
public class HandleOfficeCarApplyReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="公车申请事件id",required = true)
    private Integer orderId;

    @ApiModelProperty(value="操作类型：1=同意；2=拒绝；3=退回；4=修改；5=作废；6=撤回",required = true)
    private Integer operationType;

    @ApiModelProperty(value="备注（30字以内）")
    private String remark;

    @ApiModelProperty(value="修改模型（若有修改）")
    private ApplyCarOrderReq updateModel;
}
