package com.htstar.ovms.enterprise.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/11
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel(value = "导出")
public class CarItemExportReq {
    @ApiModelProperty(value="开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value="结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value="车牌号")
    private String licCode;
    @ApiModelProperty(value="状态")
    private Integer itemStatus;
    @ApiModelProperty(value="企业ID")
    private Integer etpId;
    @ApiModelProperty(value = "司机名下车辆车牌")
    private List<String> licCodes;
    @ApiModelProperty(value = "录入人id")
    private Integer userId;
    @ApiModelProperty(value="导出的ids")
    private String ids;
}
