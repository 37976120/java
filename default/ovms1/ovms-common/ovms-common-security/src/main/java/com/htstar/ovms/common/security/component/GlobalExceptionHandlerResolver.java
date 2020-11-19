package com.htstar.ovms.common.security.component;

import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.core.util.WebUtils;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ovms
 * @date 2018/8/30
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerResolver {

	/**
	 * 全局异常.
	 *
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R handleGlobalException(Exception e) {
		HttpServletRequest request = WebUtils.getRequest();
		if (null != request){
			Authentication authentication = SecurityUtils.getAuthentication();
			if (null != authentication){
				OvmsUser user = SecurityUtils.getUser(authentication);
				if (null != user){
					String clientId = SecurityUtils.getClientId(authentication);
					log.error("携带token请求异常：请求用户：{}，请求客户端：{}，请求详情：{}，全局异常信息 ex={}",
							user.getUsername(),
							clientId,
							WebUtils.showParams(WebUtils.getRequest()),
							e.getMessage(), e);
				}else {
					log.error("请求异常：请求详情：{}，全局异常信息 ex={}",
							WebUtils.showParams(WebUtils.getRequest()),
							e.getMessage(), e);
				}

			}else {
				log.error("免签请求异常，请求详情：{}，全局异常信息 ex={}",
						WebUtils.showParams(WebUtils.getRequest()),
						e.getMessage(), e);
			}
		}else {
			log.error("未获取到HttpServletRequest，全局异常信息 ex={}",e.getMessage(), e);
		}
		return R.error(500,null,  "服务异常");
	}



	/**
	 * validation Exception
	 *
	 * @param e
	 * @return R
	 */
	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleBodyValidException(MethodArgumentTypeMismatchException e) {
		HttpServletRequest request = WebUtils.getRequest();
		if (null != request){
			Authentication authentication = SecurityUtils.getAuthentication();
			if (null != authentication){
				OvmsUser user = SecurityUtils.getUser(authentication);
				String clientId = SecurityUtils.getClientId(authentication);
				log.warn("请求参数异常：请求用户：{}，请求客户端：{}，请求详情：{}，全局异常信息 ex={}",
						user.getUsername(),
						clientId,
						WebUtils.showParams(WebUtils.getRequest()),
						e.getMessage(), e);
			}else {
				log.warn("请求参数异常，请求详情：{}，全局异常信息 ex={}",
						WebUtils.showParams(WebUtils.getRequest()),
						e.getMessage(), e);
			}
		}else {
			log.error("未获取到HttpServletRequest，全局异常信息 ex={}",e.getMessage(), e);
		}
		return R.failed("请正确传参");
	}

	/**
	 * AccessDeniedException
	 *
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public R handleAccessDeniedException(AccessDeniedException e) {
		String msg = SpringSecurityMessageSource.getAccessor()
				.getMessage("AbstractAccessDecisionManager.accessDenied"
						, e.getMessage());
		log.error("拒绝授权异常信息 ex={}", msg, e);
		return R.failed(e.getLocalizedMessage());
	}

	/**
	 * validation Exception
	 *
	 * @param exception
	 * @return R
	 */
	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleBodyValidException(MethodArgumentNotValidException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
		return R.failed(fieldErrors.get(0).getDefaultMessage());
	}

	/**
	 * validation Exception (以form-data形式传参)
	 *
	 * @param exception
	 * @return R
	 */
	@ExceptionHandler({BindException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R bindExceptionHandler(BindException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
		return R.failed(fieldErrors.get(0).getDefaultMessage());
	}
}
