package com.htstar.ovms.enterprise.api.vo;

import com.htstar.ovms.enterprise.api.entity.CarOtherItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aspectj.weaver.ast.Or;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/20
 * Company: 航通星空
 * Modified By:
 */
@Data
public class CarOtherItemPageVO extends CarOtherItem {
    @ApiModelProperty(value="车牌号")
    private String licCode ;
    @ApiModelProperty(value="录入用户")
    private String username ;
    @ApiModelProperty(value = "用车记录编号")
    private String useCarOrder;
}
