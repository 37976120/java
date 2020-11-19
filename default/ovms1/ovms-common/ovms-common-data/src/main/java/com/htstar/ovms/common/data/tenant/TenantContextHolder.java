package com.htstar.ovms.common.data.tenant;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * @author ovms
 * @date 2018/10/4 租户工具类
 */
@UtilityClass
public class TenantContextHolder {

	private final ThreadLocal<Integer> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();

	/**
	 * TTL 设置租户ID<br/>
	 * <b>谨慎使用此方法,避免嵌套调用。尽量使用 {@code TenantBroker} </b>
	 * @param etpId
	 * @see TenantBroker
	 */
	public void setEtpId(Integer etpId) {
		THREAD_LOCAL_TENANT.set(etpId);
	}

	/**
	 * 获取TTL中的租户ID
	 * @return
	 */
	public Integer getEtpId() {
		return THREAD_LOCAL_TENANT.get();
	}

	public void clear() {
		THREAD_LOCAL_TENANT.remove();
	}

}
