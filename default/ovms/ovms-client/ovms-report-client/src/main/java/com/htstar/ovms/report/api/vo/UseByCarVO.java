package com.htstar.ovms.report.api.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/8/4
 * Company: 航通星空
 * Modified By:
 */
@Data
public class UseByCarVO {
    private IPage<CarReportVO> page;

    @ApiModelProperty(value="出车次数总计")
    private int useTotal;

    @ApiModelProperty(value="费用总计（元）")
    private String costTotal;
}
