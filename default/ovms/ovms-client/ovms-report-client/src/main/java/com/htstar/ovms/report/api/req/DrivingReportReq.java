package com.htstar.ovms.report.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description:
 * Author: JinZhu
 * Date: Created in 2020/7/27
 * Company: 航通星空
 * Modified By:
 */
@Data
public class DrivingReportReq {

    @ApiModelProperty(value="年份")
    @NotBlank(message = "请传入年份")
    private String yearDate;

    @ApiModelProperty(value="车牌号")
    private String licCode;

    @ApiModelProperty(value="导出状态：0=正常查询（默认，可以不传）；1=导出")
    private int exportStatus;
}
