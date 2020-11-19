package com.htstar.ovms.enterprise.api.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * @Author: lw
 * Date: Created in 2020/7/23
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel("流水查询请求参数")
public class CardCostInfoReq extends Page {
    @ApiModelProperty("卡类型,0:油卡 1:etc卡")
    private Integer cardType;
    @ApiModelProperty("卡编号")
    private Integer cardId;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("操作类型0:充值 1:修改 2:消费")
    private Integer actionType;



}
