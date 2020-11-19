package com.htstar.ovms.admin.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/9
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel(value = "app菜单树")
@EqualsAndHashCode(callSuper = true)
public class AppMenuTree  extends TreeNode implements Serializable {
    @ApiModelProperty(value = "菜单名称")
    private String name;

}
