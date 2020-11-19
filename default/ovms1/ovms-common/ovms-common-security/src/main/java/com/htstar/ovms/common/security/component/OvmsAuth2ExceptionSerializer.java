package com.htstar.ovms.common.security.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.security.exception.OvmsAuth2Exception;
import lombok.SneakyThrows;

/**
 * OAuth2 异常格式化
 */
public class OvmsAuth2ExceptionSerializer extends StdSerializer<OvmsAuth2Exception> {
	public OvmsAuth2ExceptionSerializer() {
		super(OvmsAuth2Exception.class);
	}

	@Override
	@SneakyThrows
	public void serialize(OvmsAuth2Exception value, JsonGenerator gen, SerializerProvider provider) {
		gen.writeStartObject();
		gen.writeObjectField("code", CommonConstants.FAIL);
		gen.writeStringField("msg", value.getMessage());
		gen.writeStringField("data", value.getErrorCode());
		gen.writeEndObject();
	}
}
