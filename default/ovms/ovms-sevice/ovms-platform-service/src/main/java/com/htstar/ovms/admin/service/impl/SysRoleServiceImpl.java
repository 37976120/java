package com.htstar.ovms.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.entity.SysRole;
import com.htstar.ovms.admin.api.entity.SysRoleMenu;
import com.htstar.ovms.admin.mapper.SysRoleMapper;
import com.htstar.ovms.admin.mapper.SysRoleMenuMapper;
import com.htstar.ovms.admin.service.SysRoleService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.core.util.RoleUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ovms
 * @since 2017-10-29
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
	private SysRoleMenuMapper sysRoleMenuMapper;

	/**
	 * 通过用户ID，查询角色信息
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public List findRolesByUserId(Integer userId) {
		return baseMapper.listRolesByUserId(userId);
	}

	/**
	 * 通过角色ID，删除角色,并清空角色菜单缓存
	 *
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeRoleById(Integer id) {
		sysRoleMenuMapper.delete(Wrappers
			.<SysRoleMenu>update().lambda()
			.eq(SysRoleMenu::getRoleId, id));
		return this.removeById(id);
	}

    @Override
    public Integer getRoleIdByCode(String roleCode, Integer etpId) {
        return baseMapper.getRoleIdByCode(roleCode, etpId);
    }

    /**
     * 保存角色 返回id
     * @param sysRole
     * @return
     */
    @Override
    public Integer saveRole(SysRole sysRole) {
        int id = baseMapper.insert(sysRole);
        return id;
    }

	@Override
	public R updateRole(SysRole req) {
    	if (RoleUtil.judeDefaltRole(req.getRoleCode())){
    		return R.failed("默认角色不允许修改");
		}
		this.updateById(req);
		return R.ok();
	}
}
