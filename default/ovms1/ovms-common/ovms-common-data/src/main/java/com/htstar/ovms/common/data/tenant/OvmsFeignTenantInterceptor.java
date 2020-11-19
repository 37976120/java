package com.htstar.ovms.common.data.tenant;

import com.htstar.ovms.common.core.constant.CommonConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ovms
 * @date 2018/9/14
 */
@Slf4j
public class OvmsFeignTenantInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		if (TenantContextHolder.getEtpId() == null) {
			log.debug("TTL 中的 租户ID为空，feign拦截器 >> 跳过");
			return;
		}
		requestTemplate.header(CommonConstants.ETP_ID, TenantContextHolder.getEtpId().toString());
	}

}
