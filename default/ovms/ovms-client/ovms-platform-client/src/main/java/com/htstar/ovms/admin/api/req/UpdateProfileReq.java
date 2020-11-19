package com.htstar.ovms.admin.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/4
 * Company: 航通星空
 * Modified By:
 * @author liuwei
 */
@Data
@ApiModel("修改个人资料信息")
public class UpdateProfileReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="用户名（只允许手机号）", required = true)
    private String username;
    @ApiModelProperty("新姓名")
    private String nickName;
    @ApiModelProperty("性别")
    private Integer sex;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty(value="短信验证码")
    private String code;
}
