package com.htstar.ovms.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.htstar.ovms.common.security.component.OvmsAuth2ExceptionSerializer;

/**
 * @author ovms
 * @date 2018/7/8
 */
@JsonSerialize(using = OvmsAuth2ExceptionSerializer.class)
public class InvalidException extends OvmsAuth2Exception {

	public InvalidException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_exception";
	}

	@Override
	public int getHttpErrorCode() {
		return 426;
	}

}
