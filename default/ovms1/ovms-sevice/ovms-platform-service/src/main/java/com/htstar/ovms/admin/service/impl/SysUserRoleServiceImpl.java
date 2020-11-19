package com.htstar.ovms.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.entity.SysUserRole;
import com.htstar.ovms.admin.mapper.SysUserRoleMapper;
import com.htstar.ovms.admin.service.SysRoleService;
import com.htstar.ovms.admin.service.SysUserRoleService;
import com.htstar.ovms.common.core.constant.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author ovms
 * @since 2017-10-29
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Autowired
    private SysRoleService roleService;



    /**
     * 根据用户Id删除该用户的角色关系
     *
     * @param userId 用户ID
     * @return boolean
     * @author 寻欢·李
     * @date 2017年12月7日 16:31:38
     */
    @Override
    public Boolean deleteByUserId(Integer userId) {
        return baseMapper.deleteByUserId(userId);
    }



    /**
     * Description: 保存用户为司机
     * Author: flr
     * Company: 航通星空
     */
    @Override
    public Boolean saveDriver(Integer userId, Integer etpId) {
        Integer roleId = roleService.getRoleIdByCode(CommonConstants.ROLE_DRIVER, etpId);
        if (null == roleId) {
            return false;
        }
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(roleId);
        sysUserRole.setUserId(userId);
        return this.save(sysUserRole);
    }

    @Override
    public Boolean saveOneRoleByCode(String roleCode, Integer userId, Integer etpId) {
        Integer roleId = roleService.getRoleIdByCode(roleCode, etpId);
        if (null == roleId) {
            return false;
        }
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(roleId);
        sysUserRole.setUserId(userId);
        return this.save(sysUserRole);
    }
    /**
     * 删除司机用户
     * @param userId
     * @param etpId
     * @return
     */
    @Override
    public Boolean removeDriver(String roleCode,Integer userId, Integer etpId) {
        Integer roleId = roleService.getRoleIdByCode(roleCode, etpId);
        if (null == roleId) {
            return false;
        }
        return this.remove(new QueryWrapper<SysUserRole>()
                .eq("user_id",userId )
                .eq("role_id",roleId ));
    }


	/**
	 * Description: 获取企业当前所有有效的管理员的ID
	 * @param etpId 企业ID 必填
	 * Author: flr
	 * Date: 2020/7/1 15:05
	 * Company: 航通星空
	 * Modified By:
	 */
	@Override
	public List<Integer> queryAdminIdList(Integer etpId) {
		return baseMapper.queryAdminIdList(etpId,CommonConstants.ROLE_ADMIN);
	}
}
