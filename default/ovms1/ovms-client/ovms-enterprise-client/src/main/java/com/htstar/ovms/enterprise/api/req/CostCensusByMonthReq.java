package com.htstar.ovms.enterprise.api.req;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/30
 * Company: 航通星空 审批记录
 * Modified By:
 */
@Data
@ApiModel(value = "月份费用统计查询条件  ")
public class CostCensusByMonthReq {
    @ApiModelProperty(value = "年份")
    private Integer year=DateUtil.year(DateUtil.date());
    @ApiModelProperty(value = "车牌号")
    private String licCode;
    @ApiModelProperty(value = "企业id")
    private Integer etpId;

}
