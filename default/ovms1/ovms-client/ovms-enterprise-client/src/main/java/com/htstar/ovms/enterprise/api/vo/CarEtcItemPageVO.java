package com.htstar.ovms.enterprise.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.htstar.ovms.enterprise.api.entity.CarEtcItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/22
 * Company: 航通星空
 * Modified By:
 */
@Data
public class CarEtcItemPageVO extends CarEtcItem {
    @ApiModelProperty(value="车牌号")
    private String licCode ;
    @ApiModelProperty(value="录入用户")
    private String username ;


}
