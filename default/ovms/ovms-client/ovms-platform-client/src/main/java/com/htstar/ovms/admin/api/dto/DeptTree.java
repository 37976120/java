package com.htstar.ovms.admin.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
