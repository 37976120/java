package com.htstar.ovms.admin.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.admin.api.dto.AppMenuNameDto;
import com.htstar.ovms.admin.api.dto.AppMenuTree;
import com.htstar.ovms.admin.api.entity.SysMenu;
import com.htstar.ovms.admin.api.vo.MenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author ovms
 * @since 2017-10-29
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

	/**
	 * 通过角色编号查询菜单
	 *
	 * @param roleId 角色ID
	 * @return
	 */
	List<MenuVO> listMenusByRoleId(Integer roleId);

	/**
	 * 通过角色ID查询权限
	 *
	 * @param roleIds Ids
	 * @return
	 */
	List<String> listPermissionsByRoleIds(String roleIds);

	@SqlParser(filter = true)
    List<SysMenu> queryEtpMenuList();


    /**
     * app端菜单
     * @param list
     * @param
     * @return
     */
    @SqlParser(filter = true)
    List<SysMenu> getAppMenuTree(@Param("list") List<String> list);

	List<SysMenu> queryEtpMenuLists(@Param("etpId")Integer etpId);

	/**
	 * 根据菜单id找菜单名称
	 * @param ints
	 * @return
	 */
    List<SysMenu> getMenuIdName(@Param("ints") int[] ints);

	List<SysMenu> queryEtpMenuListsEtpId(@Param("menuIds") int[] menuIds);
}
