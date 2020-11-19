package com.htstar.ovms.admin.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description: 用户消息所需
 * Author: flr
 * Date: Created in 2020/8/13
 * Company: 航通星空
 * Modified By:
 */
@Data
public class UserMsgVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer userId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "性别 0:女 1:男")
    private Integer sex;

    @ApiModelProperty(value="姓名")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "所属租户")
    private Integer etpId;

}

