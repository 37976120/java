package com.htstar.ovms.admin.controller;

import com.htstar.ovms.admin.service.MobileService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 手机号码登陆（/mobile已经被网关拦截，所以此url只做登陆相关的）
 */
@RestController
@AllArgsConstructor
@RequestMapping("/mobile")
@Api(value = "手机号登录", tags = "手机号登录")
public class MobileController {
	private final MobileService mobileService;
    /**
     * 已存在的用户发送短信（用于用户登陆）
     * @param mobile
     * @return
     */
	@ApiOperation(value = "已存在的用户发送短信", notes = "用于用户登陆")
	@Inner(value = false)
	@GetMapping("/{mobile}")
	public R sendSmsCodeForExistUser(@PathVariable String mobile) {
		return mobileService.sendSmsCodeForExistUser(mobile);
	}

}
