package com.htstar.ovms.enterprise.api.vo;

import com.htstar.ovms.enterprise.api.entity.CarEtcCard;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import sun.dc.pr.PRError;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/23
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel(value = "油卡分页结果")
public class EtcCardPageVO extends CarEtcCard {
    @ApiModelProperty(value="绑定对象")
    private String bindingUser;
}
