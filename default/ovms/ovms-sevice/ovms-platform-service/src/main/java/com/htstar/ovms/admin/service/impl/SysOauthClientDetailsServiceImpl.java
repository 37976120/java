package com.htstar.ovms.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.entity.SysOauthClientDetails;
import com.htstar.ovms.admin.mapper.SysOauthClientDetailsMapper;
import com.htstar.ovms.admin.service.SysOauthClientDetailsService;
import com.htstar.ovms.common.core.constant.CacheConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ovms
 * @since 2018-05-15
 */
@Service
public class SysOauthClientDetailsServiceImpl extends ServiceImpl<SysOauthClientDetailsMapper, SysOauthClientDetails> implements SysOauthClientDetailsService {

	/**
	 * 通过ID删除客户端
	 *
	 * @param clientId
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId")
	public Boolean removeByClientId(String clientId) {
		return this.remove(Wrappers.<SysOauthClientDetails>lambdaQuery()
				.eq(SysOauthClientDetails::getClientId, clientId));
	}

	/**
	 * 根据客户端信息
	 *
	 * @param clientDetails
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientDetails.clientId")
	public Boolean updateClientById(SysOauthClientDetails clientDetails) {
		return this.updateById(clientDetails);
	}
}
