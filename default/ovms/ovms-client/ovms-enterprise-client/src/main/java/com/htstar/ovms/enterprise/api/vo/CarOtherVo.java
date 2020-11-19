package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/13
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel("费用批量登记")
public class CarOtherVo implements Serializable {
    @ApiModelProperty("费用类型")
    private Integer costType;
    @ApiModelProperty("项目金额")
    private Integer itemMoney;
    @ApiModelProperty("票据地址")
    private String billAddr;
}
