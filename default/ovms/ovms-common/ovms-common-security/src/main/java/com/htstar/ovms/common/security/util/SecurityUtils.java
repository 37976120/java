package com.htstar.ovms.common.security.util;


import cn.hutool.core.util.StrUtil;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.security.service.OvmsUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SecurityUtils {
	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 *
	 * @param authentication
	 * @return OvmsUser
	 * <p>
	 */
	public OvmsUser getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof OvmsUser) {
			return (OvmsUser) principal;
		}
		return null;
	}

	/**
	 * 获取用户
	 */
	public OvmsUser getUser() {
		Authentication authentication = getAuthentication();
		return getUser(authentication);
	}
	/**
	 * 获取用户角色信息
	 *
	 * @return 角色集合
	 */
	public List<Integer> getRoles() {
		Authentication authentication = getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<Integer> roleIds = new ArrayList<>();
		authorities.stream()
				.filter(granted -> StrUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
				.forEach(granted -> {
					String id = StrUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
					roleIds.add(Integer.parseInt(id));
				});
		return roleIds;
	}


	/**
	 * 获取客户端
	 *
	 * @return clientId
	 */
	public String getClientId() {
		Authentication authentication = getAuthentication();
		if (authentication instanceof OAuth2Authentication) {
			OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
			return auth2Authentication.getOAuth2Request().getClientId();
		}
		return null;
	}

	/**
	 * 获取客户端
	 *
	 * @return clientId
	 */
	public String getClientId(Authentication authentication) {
		if (authentication instanceof OAuth2Authentication) {
			OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
			return auth2Authentication.getOAuth2Request().getClientId();
		}
		return null;
	}


	/**
	 * 获取用户名称
	 *
	 * @return username
	 */
	public String getUsername() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
       return authentication.getName();
	}
    /**
     * 判断是否是app登陆
     * @param
     * @return
     */
    public Boolean isAppClient(){
        String client = getClientId();
        if ("app".equals(client)){
            return true;
        }
        return false;
    }


    /**
     * 获取最高权限
     * @param user
     * @return
     */
    public  List<String> getRoleCode(OvmsUser user) {
        String[] roleCodes = user.getRoleCodes();
        List<String> list =  Arrays.asList(roleCodes);
        return list;

    }
}
