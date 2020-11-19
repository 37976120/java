package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/20
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel("项目到期推送内容")
public class ItemPushVO implements Serializable {
    @ApiModelProperty("车牌号")
    private String licCode;
    @ApiModelProperty("到期时间")
    private String timeValue;
    @ApiModelProperty("保险类型")
    private Integer insType;
    @ApiModelProperty("推送用户id")
    private Integer userId;
}
