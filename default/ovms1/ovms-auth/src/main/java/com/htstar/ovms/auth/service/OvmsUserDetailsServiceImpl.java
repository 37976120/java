package com.htstar.ovms.auth.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.htstar.ovms.admin.api.dto.UserInfo;
import com.htstar.ovms.admin.api.entity.SysUser;
import com.htstar.ovms.admin.api.feign.RemoteUserService;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.exception.OvmsLoginException;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.service.OvmsUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户详细信息
 *
 * @author ovms
 */
@Slf4j
@Service
@AllArgsConstructor
public class OvmsUserDetailsServiceImpl implements OvmsUserDetailsService {
	private final RemoteUserService remoteUserService;
	private final CacheManager cacheManager;

	/**
	 * 用户密码登录
	 *
	 * @param username 用户名
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String username) {
		Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
		if (cache != null && cache.get(username) != null) {
			return (OvmsUser) cache.get(username).get();
		}

		R<UserInfo> result = remoteUserService.info(username, SecurityConstants.FROM_IN);
		UserDetails userDetails = getUserDetails(result);
		cache.put(username, userDetails);
		return userDetails;
	}


	/**
	 * 根据社交登录code 登录
	 *
	 * @param inStr TYPE@CODE
	 * @return UserDetails
	 * @throws UsernameNotFoundException
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserBySocial(String inStr) {
		return getUserDetails(remoteUserService.social(inStr, SecurityConstants.FROM_IN));
	}

	/**
	 * 构建userdetails
	 *
	 * @param result 用户信息
	 * @return
	 */
	private UserDetails getUserDetails(R<UserInfo> result) throws AuthenticationException, OvmsLoginException {
		if (result == null) {
			throw new UsernameNotFoundException("用户不存在");
		}
		//自定义错误提示抛出 https://www.jianshu.com/p/b0cc88574f5d
		if (result.getCode() == CommonConstants.FAIL){
			throw new OvmsLoginException(result.getMsg());
		}

		if (result == null) {
			throw new UsernameNotFoundException("用户不存在");
		}

		UserInfo info = result.getData();
		Set<String> dbAuthsSet = new HashSet<>();
		if (ArrayUtil.isNotEmpty(info.getRoles())) {
			// 获取角色
			Arrays.stream(info.getRoles()).forEach(roleId -> dbAuthsSet.add(SecurityConstants.ROLE + roleId));
			// 获取资源
			dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

		}
		Collection<? extends GrantedAuthority> authorities
				= AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
		SysUser user = info.getSysUser();
		boolean enabled = StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL);

		TenantContextHolder.setEtpId(user.getEtpId());

		// 构造security用户
		return new OvmsUser(user.getUserId(), user.getDeptId(), user.getPhone(), user.getAvatar(), user.getEtpId()
				, user.getUsername(), SecurityConstants.BCRYPT + user.getPassword()
				, enabled, true, true
				, !CommonConstants.STATUS_LOCK.equals(user.getLockFlag()), authorities,info.getRoleCodes());
	}
}
