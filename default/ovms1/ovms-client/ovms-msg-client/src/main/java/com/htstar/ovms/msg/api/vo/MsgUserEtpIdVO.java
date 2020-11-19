package com.htstar.ovms.msg.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author JinZhu
 * @Description:
 * @date 2020/7/2815:23
 */
@Data
@ApiModel(value = "推送企业下的所有用户映射")
public class MsgUserEtpIdVO implements Serializable {

    @ApiModelProperty(value="企业名称")
    private String etpName;

    @ApiModelProperty(value="用户名称")
    private String username;

    @ApiModelProperty(value="用户id")
    private Integer userId;



}
