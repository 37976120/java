package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/1
 * Company: 航通星空
 * Modified By:
 */
@Data
public class PhotoVO {

    /**
     * 图片路径
     */
    @ApiModelProperty(value="图片路径")
    private String photoUrl;
}
