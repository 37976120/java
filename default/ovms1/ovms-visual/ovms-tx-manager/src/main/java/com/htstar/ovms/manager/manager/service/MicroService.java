package com.htstar.ovms.manager.manager.service;

import com.htstar.ovms.manager.model.TxServer;
import com.htstar.ovms.manager.model.TxState;

/**
 * @author LCN on 2017/11/11
 */
public interface MicroService {

	String TMKEY = "tx-manager";

	TxServer getServer();

	TxState getState();
}
