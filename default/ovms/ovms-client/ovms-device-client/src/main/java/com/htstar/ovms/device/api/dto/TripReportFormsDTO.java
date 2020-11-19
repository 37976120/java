package com.htstar.ovms.device.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HanGuJi
 * @Description: 行驶报表查询条件
 * @date 2020/7/2214:21
 */
@Data
public class TripReportFormsDTO extends Page {

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "年份")
    private String year;

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "企业id")
    private Integer etpId;
}
