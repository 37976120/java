package com.htstar.ovms.enterprise.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.htstar.ovms.enterprise.api.entity.CarFuelItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/18
 * Company: 航通星空
 * Modified By:
 */
@Data
public class CarFuelItemPageVO extends CarFuelItem {
    @ApiModelProperty(value="车牌号")
    private String licCode ;
    @ApiModelProperty(value="录入用户")
    private String username ;
}
