package com.htstar.ovms.manager.manager.service;

import com.htstar.ovms.manager.model.LoadBalanceInfo;

/**
 * @author LCN on 2017/12/5
 */
public interface LoadBalanceService {

	boolean put(LoadBalanceInfo loadBalanceInfo);

	LoadBalanceInfo get(String groupId, String key);

	boolean remove(String groupId);

	String getLoadBalanceGroupName(String groupId);

}
