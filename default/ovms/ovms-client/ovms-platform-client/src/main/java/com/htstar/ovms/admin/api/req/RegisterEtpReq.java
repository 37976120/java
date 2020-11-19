package com.htstar.ovms.admin.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/8/8
 * Company: 航通星空
 * Modified By:
 */
@Data
public class RegisterEtpReq {

    @ApiModelProperty(value="企业名称")
    @NotBlank(message = "企业名称必填")
    private String etpName;

    @ApiModelProperty(value="管理员姓名")
    @NotBlank(message = "管理员姓名必填")
    private String adminName;

    @ApiModelProperty(value="管理员手机号")
    @NotBlank(message = "管理员手机号必填")
    private String adminMobile;

    @ApiModelProperty(value="管理员密码")
    @NotBlank(message = "管理员密码必填")
    private String password;

    @ApiModelProperty(value="企业类型：1=试用企业，2=正式企业",hidden = true)
    private Integer etpType = 1;

    @ApiModelProperty(value="企业地址")
    @NotBlank(message = "企业地址必填")
    private String etpAddr;

    @ApiModelProperty(value="手机号验证码")
    @NotBlank(message = "手机号验证码必填")
    private String mobileCode;

    @ApiModelProperty(value="所属行业（10字）")
    private String industry;
}
