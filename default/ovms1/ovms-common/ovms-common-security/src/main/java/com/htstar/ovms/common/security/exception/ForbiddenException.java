package com.htstar.ovms.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.htstar.ovms.common.security.component.OvmsAuth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

/**
 * @author ovms
 * @date 2018/7/8
 */
@JsonSerialize(using = OvmsAuth2ExceptionSerializer.class)
public class ForbiddenException extends OvmsAuth2Exception {
	public ForbiddenException(String msg) {
		super(msg);
	}
	public ForbiddenException(String msg, Throwable t) {
		super(msg,t);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "access_denied";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.FORBIDDEN.value();
	}

}

