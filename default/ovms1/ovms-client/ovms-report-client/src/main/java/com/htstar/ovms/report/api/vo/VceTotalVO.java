package com.htstar.ovms.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/30
 * Company: 航通星空
 * Modified By:
 */
@Data
public class VceTotalVO {
    /**
     *    车辆
     */
    @ApiModelProperty(value="车辆")
    private String vce;

    /**
     *    行驶公里
     */
    @ApiModelProperty(value="行驶公里")
    private int mileaGeTal;
    /**
     *    时间（h）
     */
    @ApiModelProperty(value="时间")
    private int timeLong;
    /**
     *   用油量（L）
     */
    @ApiModelProperty(value="用油量")
    private int consumPtionTal;
    /**
     *   百公里耗油（L）
     */
    @ApiModelProperty(value="百公里耗油")
    private int hdredKm;

    /**
     *   里程表数（L）
     */
    @ApiModelProperty(value="里程表数")
    private int omtNumber;
}
