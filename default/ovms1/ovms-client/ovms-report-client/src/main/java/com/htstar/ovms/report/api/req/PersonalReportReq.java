package com.htstar.ovms.report.api.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@ApiModel("用车报表按人的查询")
@Data
public class PersonalReportReq extends MyPage implements Serializable {

    @ApiModelProperty(hidden = true, value = "企业ID")
    private Integer etpId;

    @ApiModelProperty(value = "年份")
    private String year;

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(hidden = true, value = "年+月组合")
    private String monthShort;

    @ApiModelProperty(value = "查询条件：部门ID")
    private Integer deptId;

    @ApiModelProperty(value = "查询条件：用车人ID")
    private String carUserId;

    @ApiModelProperty(value="导出状态：0=正常查询（默认，可以不传）；1=导出")
    private int exportStatus;


}
