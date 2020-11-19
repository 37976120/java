package com.htstar.ovms.report.api.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/30
 * Company: 航通星空
 * Modified By:
 */
@Data
public class VceTotalPageVO {
   private IPage<VceTotalVO> page;
    @ApiModelProperty(value="行驶公里总计")
    private int totalDrivingKmCount;
    @ApiModelProperty(value="行驶时间总计")
    private int totalDrivingTimeCount;
    @ApiModelProperty(value="用油量总计")
    private int totalDrivingOilTotal;
}
