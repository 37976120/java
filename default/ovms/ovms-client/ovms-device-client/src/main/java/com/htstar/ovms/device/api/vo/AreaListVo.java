/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.htstar.ovms.device.api.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;


/**
 * 行政区划Entity
 * @author ThinkGem
 * @version 2017-03-22
 */
@Data
public class AreaListVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "区域代码")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String areaCode;


	@Length(min=0, max=100, message="名称长度不能超过 100 个字符")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String areaName;


	@ApiModelProperty(value = "区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String areaType;

	@ApiModelProperty(value = "子集递归")
	//第三级查询是没有的，为null ，所有排除掉null字段，避免过多的不需要的数据存入jvm
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<AreaListVo> areaListVos;

	
}