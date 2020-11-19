package com.htstar.ovms.auth.service;

import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * @author ovms
 * @date 2020/03/25
 * <p>
 * 扩展 JdbcClientDetailsService 支持多租户
 */
@Service
public class OvmsClientDetailsServiceImpl extends JdbcClientDetailsService {
	public OvmsClientDetailsServiceImpl(DataSource dataSource) {
		super(dataSource);
	}


	/**
	 * 重写原生方法支持redis缓存
	 *
	 * @param clientId
	 * @return ClientDetails
	 * @throws InvalidClientException
	 */
	@Override
	@Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
	public ClientDetails loadClientByClientId(String clientId) {
		super.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
		return super.loadClientByClientId(clientId);
	}
}
