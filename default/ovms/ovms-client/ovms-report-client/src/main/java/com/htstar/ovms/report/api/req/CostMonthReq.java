package com.htstar.ovms.report.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/29
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel("费用报表按月查询")
public class CostMonthReq {
    @ApiModelProperty(value="车牌号")
    private String licCode;
    @ApiModelProperty(value = "年份")
    private String year;
    @ApiModelProperty(hidden = true)
    private Integer etpId;
    @ApiModelProperty(value="导出状态：0=正常查询（默认，可以不传）；1=导出")
    private int exportStatus;


}
