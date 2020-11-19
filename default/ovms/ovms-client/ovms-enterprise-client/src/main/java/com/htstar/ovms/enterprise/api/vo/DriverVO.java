package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/7
 * Company: 航通星空
 * Modified By:
 */
@Data
public class DriverVO {
    @ApiModelProperty(value="用户id")
    private Integer userId;
    @ApiModelProperty(value="用户名（手机号）")
    private String nickName;
    @ApiModelProperty(value="姓名")
    private String username;
}
