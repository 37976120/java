package com.htstar.ovms.admin.api.dto;

import com.htstar.ovms.admin.api.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 部门树对象
 *
 * @author ovms
 * @date 2020-06-19
 */
@Data
@ApiModel(value = "部门树")
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends TreeNode {

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "部门id")
    private Integer deptId;


    /**
     * 是否显示被锁定
     */
    private Boolean isLock = true;

}
