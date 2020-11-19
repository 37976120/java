package com.htstar.ovms.common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.htstar.ovms.common.security.component.OvmsAuth2ExceptionSerializer;
import org.springframework.http.HttpStatus;

/**
 * @author ovms
 * @date 2018/7/8
 */
@JsonSerialize(using = OvmsAuth2ExceptionSerializer.class)
public class ServerErrorException extends OvmsAuth2Exception {

	public ServerErrorException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "server_error";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.INTERNAL_SERVER_ERROR.value();
	}

}
