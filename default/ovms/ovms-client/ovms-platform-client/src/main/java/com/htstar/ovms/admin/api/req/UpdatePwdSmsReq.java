package com.htstar.ovms.admin.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description: 短信验证码修改密码请求模型
 * Author: flr
 * Date: Created in 2020/6/28
 * Company: 航通星空
 * Modified By:
 */
@Data
public class UpdatePwdSmsReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="用户名（只允许手机号）", required = true)
    private String username;

    @ApiModelProperty(value="新密码", required = true)
    private String password;

    @ApiModelProperty(value="短信验证码", required = true)
    private String code;
}