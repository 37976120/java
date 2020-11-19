package com.htstar.ovms.auth.handler;

import cn.hutool.core.util.StrUtil;
import com.htstar.ovms.admin.api.entity.SysLog;
import com.htstar.ovms.admin.api.feign.RemoteLogService;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.WebUtils;
import com.htstar.ovms.common.log.util.SysLogUtils;
import com.htstar.ovms.common.security.handler.AuthenticationFailureHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ovms
 * @date 2018/10/8
 */
@Slf4j
@Component
@AllArgsConstructor
public class OvmsAuthenticationFailureEventHandler implements AuthenticationFailureHandler {
	private final RemoteLogService logService;

	/**
	 * 异步处理，登录失败方法
	 * <p>
	 *
	 * @param authenticationException 登录的authentication 对象
	 * @param authentication          登录的authenticationException 对象
	 * @param request                 请求
	 * @param response                响应
	 */
	@Async
	@Override
	@SneakyThrows
	public void handle(AuthenticationException authenticationException, Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		String tenantId = request.getHeader(CommonConstants.ETP_ID);

		if (StrUtil.isNotBlank(tenantId)) {
			String username = authentication.getName();
			SysLog sysLog = SysLogUtils.getSysLog(request, username);
			sysLog.setTitle(username + "用户登录");
			sysLog.setType(CommonConstants.STATUS_LOCK);
			sysLog.setParams(username);
			sysLog.setException(authenticationException.getLocalizedMessage());

			if (StrUtil.isNotBlank(header)) {
				sysLog.setServiceId(WebUtils.getClientId(header));
			}

			logService.saveLog(sysLog, SecurityConstants.FROM_IN);
		}



		log.info("用户：{} 登录失败，异常：{}", authentication.getName(), authenticationException.getLocalizedMessage());
	}
}
