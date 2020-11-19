package com.htstar.ovms.report.api.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @Author: lw
 * Date: Created in 2020/7/29
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel("年份月份查询")
public class CarReportReq extends Page {
    @ApiModelProperty(hidden = true, value = "企业ID")
    private Integer etpId;
    @ApiModelProperty(value = "年份")
    private String year;
    @ApiModelProperty(value = "月份")
    private String month;
    @ApiModelProperty(hidden = true, value = "年+月组合")
    private String monthShort;
    @ApiModelProperty(value = "导出状态：0=正常查询（默认，可以不传）；1=导出")
    private int exportStatus;

    @ApiModelProperty(value = "查询条件：部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "查询条件：用车人ID")
    private String carUserId;
}
