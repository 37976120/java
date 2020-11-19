package com.htstar.ovms.admin.controller;

import com.htstar.ovms.admin.api.feign.RemoteTokenService;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/token")
@Api(value = "token", tags = "令牌管理模块")
public class SysTokenController {
	private final RemoteTokenService remoteTokenService;
	@ApiOperation(value = "分页TOKEN信息", notes = "分页TOKEN信息")
	@GetMapping("/page")
	public R getTokenPage(@RequestParam Map<String, Object> params) {
		return remoteTokenService.getTokenPage(params, SecurityConstants.FROM_IN);
	}
	@ApiOperation(value = "登出", notes = "登出")
	@SysLog("删除用户TOKEN")
	@DeleteMapping("/{token}")
	public R removeById(@PathVariable String token) {
		return remoteTokenService.removeTokenById(token, SecurityConstants.FROM_IN);
	}
}
