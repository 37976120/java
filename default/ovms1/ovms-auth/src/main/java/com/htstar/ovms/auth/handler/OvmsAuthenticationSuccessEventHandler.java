package com.htstar.ovms.auth.handler;

import com.htstar.ovms.common.security.handler.AuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
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
public class OvmsAuthenticationSuccessEventHandler implements AuthenticationSuccessHandler {
//	private final RemoteLogService logService;

	/**
	 * 处理登录成功方法
	 * <p>
	 * 获取到登录的authentication 对象
	 *
	 * @param authentication 登录对象
	 * @param request        请求
	 * @param response       返回
	 */
	@Async
	@Override
	public void handle(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
		String username = authentication.getName();
//		SysLog sysLog = SysLogUtils.getSysLog(request, username);
//		sysLog.setTitle(username + "用户登录");
//		sysLog.setParams(username);
//		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
//		if (StrUtil.isNotBlank(header)) {
//			sysLog.setServiceId(WebUtils.getClientId(header));
//		}

//		logService.saveLog(sysLog, SecurityConstants.FROM_IN);
		log.info("用户：{} 登录成功", username);
	}
}
