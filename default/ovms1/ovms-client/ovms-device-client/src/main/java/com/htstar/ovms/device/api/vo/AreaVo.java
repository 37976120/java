/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.htstar.ovms.device.api.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 行政区划Entity
 * @author ThinkGem
 * @version 2017-03-22
 */
@Data
public class AreaVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "区域代码")
	private String areaCode;


	@Length(min=0, max=100, message="名称长度不能超过 100 个字符")
	private String areaName;


	@ApiModelProperty(value = "区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）")
	private String areaType;

	
}