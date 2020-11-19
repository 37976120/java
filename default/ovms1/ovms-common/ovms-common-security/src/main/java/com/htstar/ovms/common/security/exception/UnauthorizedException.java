package com.htstar.ovms.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.htstar.ovms.common.security.component.OvmsAuth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

/**
 * @author ovms
 * @date 2018/7/8
 */
@JsonSerialize(using = OvmsAuth2ExceptionSerializer.class)
public class UnauthorizedException extends OvmsAuth2Exception {

	public UnauthorizedException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "unauthorized";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.UNAUTHORIZED.value();
	}

}
