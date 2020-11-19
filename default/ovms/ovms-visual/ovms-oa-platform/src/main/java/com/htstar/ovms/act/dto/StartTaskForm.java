package com.htstar.ovms.act.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author felixu
 * @Date 2018.08.14
 */
@Data
public class StartTaskForm {
	@NotEmpty
	public String procDefKey;
	public String entityId;
	@NotEmpty
	public String title;
	public String comment;
}
