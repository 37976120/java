package com.htstar.ovms.enterprise.api.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javafx.scene.chart.ValueAxis;
import lombok.Data;
import lombok.Value;

import java.util.Date;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/5
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel(value = "审批记录分页请求参数")
public class ApprovalRecordReq extends Page {
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty("车牌号")
    private String licCode;
    @ApiModelProperty(hidden = true,value = "企业id")
    private Integer etpId;
    @ApiModelProperty("审批状态")
    private Integer operationType;
    @ApiModelProperty("主键Id")
    private Integer id;
}
