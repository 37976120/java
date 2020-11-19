package com.htstar.ovms.admin.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description: 申请加入企业
 * Author: flr
 * Date: Created in 2020/6/28
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ApplyJoinReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="部门ID（可选）", required = false)
    private Integer deptId;

    @ApiModelProperty(value="企业Id", required = true)
    private Integer etpId;

    @ApiModelProperty(value="手机号", required = true)
    private String mobile;

    @ApiModelProperty(value="验证码", required = true)
    private String code;

    @ApiModelProperty(value="姓名", required = true)
    private String nickName;

    @ApiModelProperty(value="密码（6-20位）", required = true)
    private String password;

    @ApiModelProperty(value = "性别 0:女 1:男")
    private Integer sex;


    @ApiModelProperty(value="是否是司机：0=否（默认），1=是", required = true)
    private int driverStatus;
}
