package com.htstar.ovms.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.admin.api.entity.SysRole;
import com.htstar.ovms.common.core.util.R;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ovms
 * @since 2017-10-29
 */
public interface SysRoleService extends IService<SysRole> {

	/**
	 * 通过用户ID，查询角色信息
	 *
	 * @param userId
	 * @return
	 */
	List<SysRole> findRolesByUserId(Integer userId);

	/**
	 * 通过角色ID，删除角色
	 *
	 * @param id
	 * @return
	 */
	Boolean removeRoleById(Integer id);

	/**
	 * Description: 通过角色code找到角色ID
	 * Author: flr
	 * Date: 2020/7/1 16:32
	 * Company: 航通星空
	 * Modified By:
	 */
    Integer getRoleIdByCode(String roleCode, Integer etpId);

    /**
     * 保存角色 返回id
     * @param sysRole
     * @return
     */
    Integer saveRole(SysRole sysRole);

    R updateRole(SysRole sysRole);
}
