package com.htstar.ovms.report.api.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class VceDrivingReportlVO {
    /**
     * 月份
     */
    @ApiModelProperty(value = "车辆名称")
    private String vce;
    /**
     * 每月行驶总行驶公里
     */
    @ApiModelProperty(value="每辆车行驶总行驶公里")
    private int drivingKmCount;
    /**
     * 每月行驶总时间
     */
    @ApiModelProperty(value="每辆车行驶总时间")
    private int drivingTimeCount;
    /**
     * 每月行驶总行驶油量
     */
    @ApiModelProperty(value="每辆车行驶总行驶油量")
    private int drivingOilTotal;
    /**
     * 每月行驶总百公里耗油量
     */
    @ApiModelProperty(value="每辆车行驶总百公里耗油量")
    private int drivingHkmOilTotal;

    /**
     *   里程表数（L）
     */
    @ApiModelProperty(value="每辆车里程表数")
    private int drivingomtNumber;
}
