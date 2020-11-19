package com.htstar.ovms.admin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.admin.api.entity.SysRole;
import com.htstar.ovms.admin.api.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色菜单表 Mapper 接口
 * </p>
 *
 * @author ovms
 * @since 2017-10-29
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {


}
