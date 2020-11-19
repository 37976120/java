package com.htstar.ovms.admin.api.feign;

import com.htstar.ovms.admin.api.dto.UserInfo;
import com.htstar.ovms.admin.api.entity.SysUser;
import com.htstar.ovms.admin.api.vo.UserMsgVO;
import com.htstar.ovms.admin.api.vo.UserVO;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ovms
 * @date 2018/6/22
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.PLATFORM_SERVICE)
public interface RemoteUserService {
	/**
	 * 通过用户名查询用户、角色信息
	 *
	 * @param username 用户名
	 * @param from     调用标志
	 * @return R
	 */
	@GetMapping("/user/info/{username}")
	R<UserInfo> info(@PathVariable("username") String username
			, @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 通过ID查询用户信息
	 *
	 * @param id ID
	 * @return 用户信息
	 */
	@GetMapping("/user/{id}")
	R<UserVO> user(@PathVariable("id") Integer id);

	/**
	 * 通过社交账号或手机号查询用户、角色信息
	 *
	 * @param inStr appid@code
	 * @param from  调用标志
	 * @return
	 */
	@GetMapping("/social/info/{inStr}")
	R<UserInfo> social(@PathVariable("inStr") String inStr
			, @RequestHeader(SecurityConstants.FROM) String from);

	/**
	 * 查询上级部门的用户信息
	 *
	 * @param username 用户名
	 * @return R
	 */
	@GetMapping("/user/ancestor/{username}")
	R<List<SysUser>> ancestorUsers(@PathVariable("username") String username);


	@GetMapping("/user/getUserMsgVOListByRoleId/{roleId}")
	List<UserMsgVO> getUserMsgVOListByRoleId(@PathVariable("roleId") Integer roleId,
											 @RequestHeader(SecurityConstants.FROM) String from);
	@PostMapping("/user/getUserMsgVOListByIds")
    List<UserMsgVO> getUserMsgVOListByIds(@RequestBody List<Integer> userIdList,
										  @RequestHeader(SecurityConstants.FROM) String from);
}
