package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/30
 * Company: 航通星空
 * Modified By:
 * @author liuwei
 */
@Data
@ApiModel(value = "某月费用结果")
public class CostCensusByMonthVO {
    @ApiModelProperty(value="月份")
    private Integer month;
    @ApiModelProperty(value="花费")
    private Integer cost;
}
