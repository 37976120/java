package com.htstar.ovms.manager.api.service.impl;

import com.htstar.ovms.manager.api.service.ApiModelService;
import com.htstar.ovms.manager.manager.ModelInfoManager;
import com.htstar.ovms.manager.model.ModelInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LCN on 2017/11/13
 * @author LCN
 */
@Service
public class ApiModelServiceImpl implements ApiModelService {


	@Override
	public List<ModelInfo> onlines() {
		return ModelInfoManager.getInstance().getOnlines();
	}


}
