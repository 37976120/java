package com.htstar.ovms.common.data.tenant;

import com.htstar.ovms.common.core.constant.CommonConstants;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author ovms
 * @date 2020/4/29
 * <p>
 * 传递 RestTemplate 请求的租户ID
 */
public class TenantRequestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		request.getHeaders().set(CommonConstants.ETP_ID, String.valueOf(TenantContextHolder.getEtpId()));
		return execution.execute(request, body);
	}

}
