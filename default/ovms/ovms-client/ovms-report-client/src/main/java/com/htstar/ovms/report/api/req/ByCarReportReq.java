package com.htstar.ovms.report.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description:
 * Author: flr
 * Date: 2020/8/4 11:43
 * Company: 航通星空
 * Modified By: 
 */
@Data
public class ByCarReportReq{

    @ApiModelProperty(value="开始日期")
    @NotBlank(message = "请传入开始日期")
    private String staTime;

    @ApiModelProperty(value="结束日期")
    @NotBlank(message = "请传入结束日期")
    private String endTime;

    @ApiModelProperty(value="部门ID")
    private Integer deptId;

    @ApiModelProperty(value="导出状态：0=正常查询（默认，可以不传）；1=导出")
    private int exportStatus;

    private Integer etpId;
}
