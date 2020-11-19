package com.htstar.ovms.admin.api.feign;

import com.htstar.ovms.admin.api.entity.SysTenant;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.ServiceNameConstants;
import com.htstar.ovms.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @author ovms
 * @date 2019/6/19
 * <p>
 * 租户接口
 */
@FeignClient(contextId = "remoteTenantService", value = ServiceNameConstants.PLATFORM_SERVICE)
public interface RemoteTenantService {
	/**
	 * 查询全部有效租户
	 *
	 * @param from 内部标志
	 * @return
	 */
	@GetMapping("/tenant/list")
	R<List<SysTenant>> list(@RequestHeader(SecurityConstants.FROM) String from);

}
