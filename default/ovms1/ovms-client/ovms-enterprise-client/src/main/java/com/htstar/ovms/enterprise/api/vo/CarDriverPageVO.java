package com.htstar.ovms.enterprise.api.vo;

import com.htstar.ovms.enterprise.api.entity.CarDriverInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/23
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel(value = "司机管理分页查询结果")
public class CarDriverPageVO extends CarDriverInfo {
    @ApiModelProperty(value="姓名")
    private String username;
    @ApiModelProperty(value="手机号")
    private String phone;
    @ApiModelProperty(value="性别")
    private Integer sex;
    @ApiModelProperty(value="出车状态 0为出车状态 1为未出车")
    private Integer orderStatus;

}
