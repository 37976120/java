package com.htstar.ovms.enterprise.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/6/2314:15
 */
@Data
public class CarLocationNoPageDTO  {

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "公司Id")
    private Integer etpId = null;

    @ApiModelProperty(value = "每页显示")
    private Integer size;

    @ApiModelProperty(value = "当前页")
    private Integer current = 10;

    @ApiModelProperty(value = "总页数")
    private Integer total;

}
