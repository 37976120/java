package com.htstar.ovms.act.dto;


import javax.validation.constraints.NotBlank;

/**
 * @author ovms
 * @date 2018/9/25
 */
public class ModelForm {

	@NotBlank(message = "名称不能为空")
	private String name;

	@NotBlank(message = "标识不能为空")
	private String key;

	@NotBlank(message = "分类不能为空")
	private String category;

	@NotBlank(message = "描述不能为空")
	private String desc;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
