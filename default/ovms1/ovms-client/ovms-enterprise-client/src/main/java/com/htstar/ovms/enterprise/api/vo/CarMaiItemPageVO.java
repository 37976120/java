package com.htstar.ovms.enterprise.api.vo;

import com.htstar.ovms.enterprise.api.entity.CarFuelItem;
import com.htstar.ovms.enterprise.api.entity.CarMaiItem;
import com.htstar.ovms.enterprise.api.entity.CarMotItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/19
 * Company: 航通星空
 * Modified By:
 */
@Data
    public class CarMaiItemPageVO extends CarMaiItem {
    @ApiModelProperty(value="车牌号")
    private String licCode ;
    @ApiModelProperty(value="录入用户")
    private String username ;


}
