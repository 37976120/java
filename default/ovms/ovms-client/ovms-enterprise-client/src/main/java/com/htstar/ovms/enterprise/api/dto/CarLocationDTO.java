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
public class CarLocationDTO extends Page {

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "公司Id")
    private Integer etpId = null;
}
