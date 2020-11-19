package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/1
 * Company: 航通星空
 * Modified By:
 */
@Data
public class PassengerVO {

    /**
     * 姓名
     */
    @ApiModelProperty(value="姓名")
    private String name;

    @ApiModelProperty(value="手机号")
    private String mobile;
}
