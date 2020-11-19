package com.htstar.ovms.common.gray.feign;

import cn.hutool.core.util.StrUtil;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ovms
 * @date 2020/1/12
 * <p>
 * interceptor 请求VERSION 传递
 */
@Slf4j
public class GrayFeignRequestInterceptor implements RequestInterceptor {
	/**
	 * Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
	 *
	 * @param template
	 */
	@Override
	public void apply(RequestTemplate template) {
		String reqVersion = WebUtils.getRequest() != null
				? WebUtils.getRequest().getHeader(CommonConstants.VERSION) : null;

		if (StrUtil.isNotBlank(reqVersion)) {
			log.debug("interceptor gray add header version :{}", reqVersion);
			template.header(CommonConstants.VERSION, reqVersion);
		}
	}
}
