package com.htstar.ovms.common.data.tenant;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * @author ovms
 * @date 2018/9/14
 * interceptor 租户信息拦截
 */
@Configuration
public class OvmsFeignTenantConfiguration {
	@Bean
	public RequestInterceptor ovmsFeignTenantInterceptor() {
		return new OvmsFeignTenantInterceptor();
	}

	@Bean
	public ClientHttpRequestInterceptor ovmsTenantRequestInterceptor() {
		return new TenantRequestInterceptor();
	}
}
