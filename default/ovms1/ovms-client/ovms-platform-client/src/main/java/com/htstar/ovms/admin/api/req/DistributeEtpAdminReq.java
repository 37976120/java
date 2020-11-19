package com.htstar.ovms.admin.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description: 分配企业管理员请求
 * Author: flr
 * Date: Created in 2020/6/12
 * Company: 航通星空
 * Modified By:
 * @author liuwei
 */
@Data
public class DistributeEtpAdminReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="用户名（只允许手机号）", required = true)
    private String username;

    @ApiModelProperty(value="密码", required = true)
    private String password;

    @ApiModelProperty(value="企业ID", required = true)
    private Integer etpId;

    @ApiModelProperty(value = "用户名字")
    private String nickName;
}
