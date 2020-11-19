package com.htstar.ovms.enterprise.api.vo;

import com.htstar.ovms.enterprise.api.entity.CarInsItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.jws.WebParam;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 */
@Data
public class CarInsItemPageVO extends CarInsItem {
    @ApiModelProperty(value="车牌号")
    private String licCode ;
    @ApiModelProperty(value="录入用户")
    private String username ;
}
