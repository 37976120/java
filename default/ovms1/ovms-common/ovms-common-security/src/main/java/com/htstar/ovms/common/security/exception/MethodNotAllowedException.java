package com.htstar.ovms.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.htstar.ovms.common.security.component.OvmsAuth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

/**
 * @author ovms
 * @date 2018/7/8
 */
@JsonSerialize(using = OvmsAuth2ExceptionSerializer.class)
public class MethodNotAllowedException extends OvmsAuth2Exception {

	public MethodNotAllowedException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "method_not_allowed";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.METHOD_NOT_ALLOWED.value();
	}

}
