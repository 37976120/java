package com.htstar.ovms.common.security.component;

import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * 客户端异常处理 {@link org.springframework.security.core.AuthenticationException } 不同细化异常处理
 *
 * @author ovms
 * @date 2020-06-14
 */
@Slf4j
@Component
@AllArgsConstructor
public class ResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) {
		response.setCharacterEncoding(CommonConstants.UTF8);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		R<String> result = new R<>();
		result.setMsg(authException.getMessage());
		result.setData(authException.getMessage());
		result.setCode(CommonConstants.FAIL);

		if (authException instanceof CredentialsExpiredException
				|| authException instanceof InsufficientAuthenticationException) {
			String msg = SpringSecurityMessageSource.getAccessor().getMessage(
					"AbstractUserDetailsAuthenticationProvider.credentialsExpired", authException.getMessage(),
					Locale.CHINA);
			result.setMsg(msg);
		}

		if (authException instanceof UsernameNotFoundException) {
			String msg = SpringSecurityMessageSource.getAccessor().getMessage(
					"AbstractUserDetailsAuthenticationProvider.noopBindAccount", authException.getMessage(),
					Locale.CHINA);
			result.setMsg(msg);
		}

		response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
		PrintWriter printWriter = response.getWriter();
		printWriter.append(objectMapper.writeValueAsString(result));
	}

}
