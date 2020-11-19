package com.htstar.ovms.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.htstar.ovms.common.security.component.OvmsAuth2ExceptionSerializer;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author ovms
 * @date 2018/7/8
 * 自定义OAuth2Exception
 */
@JsonSerialize(using = OvmsAuth2ExceptionSerializer.class)
public class OvmsAuth2Exception extends OAuth2Exception {
	@Getter
	private String errorCode;

	public OvmsAuth2Exception(String msg) {
		super(msg);
	}

	public OvmsAuth2Exception(String msg, Throwable t) {
		super(msg,t);
	}

	public OvmsAuth2Exception(String msg, String errorCode) {
		super(msg);
		this.errorCode = errorCode;
	}
}
