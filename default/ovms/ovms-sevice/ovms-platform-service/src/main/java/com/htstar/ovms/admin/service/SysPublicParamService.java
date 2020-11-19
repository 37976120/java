package com.htstar.ovms.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.admin.api.entity.SysPublicParam;
import com.htstar.ovms.common.core.util.R;

/**
 * 公共参数配置
 *
 * @author Lucky
 * @date 2019-04-29
 */
public interface SysPublicParamService extends IService<SysPublicParam> {

	/**
	 * 通过key查询公共参数指定值
	 *
	 * @param publicKey
	 * @return
	 */
	String getSysPublicParamKeyToValue(String publicKey);

	/**
	 * 更新参数
	 *
	 * @param sysPublicParam
	 * @return
	 */
	R updateParam(SysPublicParam sysPublicParam);

	/**
	 * 删除参数
	 *
	 * @param publicId
	 * @return
	 */
	R removeParam(Long publicId);
}
