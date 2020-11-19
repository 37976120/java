package com.htstar.ovms.admin.service;


import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.admin.api.entity.SysUserRole;
import com.htstar.ovms.common.core.constant.CommonConstants;

import java.util.List;

/**
 * 用户角色表 服务类
 */
public interface SysUserRoleService extends IService<SysUserRole> {



	/**
	 * 根据用户Id删除该用户的角色关系
	 *
	 * @param userId 用户ID
	 * @return boolean
	 * @author 寻欢·李
	 * @date 2017年12月7日 16:31:38
	 */
	Boolean deleteByUserId(Integer userId);



	/**
	 * 保存用户为司机
	 * @param userId
	 * @param etpId
	 * @return
	 */
	Boolean saveDriver(Integer userId, Integer etpId);

	Boolean saveOneRoleByCode(String roleCode, Integer userId, Integer etpId);



    /**
     * 删除司机用户
     * @param userId
     * @param etpId
     * @return
     */
    Boolean removeDriver(String roleCode,Integer userId,Integer etpId);


	/**
	 * Description: 获取企业当前所有有效的管理员的ID
	 * @param etpId 企业ID 必填
	 * Author: flr
	 * Date: 2020/7/1 15:05
	 * Company: 航通星空
	 * Modified By:
	 */
    List<Integer> queryAdminIdList(Integer etpId);

}
