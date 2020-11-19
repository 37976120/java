package com.htstar.ovms.msg.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/8/1315:03
 */
@Data
@ApiModel(value = "个推传输对象")
public class CidUserNameDTO {

    @ApiModelProperty(value="cid")
    private String cid;
    @ApiModelProperty(value="用户账号")
    private String userName;

}
