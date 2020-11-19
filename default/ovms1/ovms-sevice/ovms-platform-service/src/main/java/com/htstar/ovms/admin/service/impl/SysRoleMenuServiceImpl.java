package com.htstar.ovms.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.dto.MenuTree;
import com.htstar.ovms.admin.api.entity.SysMenu;
import com.htstar.ovms.admin.api.entity.SysRole;
import com.htstar.ovms.admin.api.entity.SysRoleMenu;
import com.htstar.ovms.admin.api.vo.TreeUtil;
import com.htstar.ovms.admin.mapper.SysRoleMenuMapper;
import com.htstar.ovms.admin.service.EtpInfoService;
import com.htstar.ovms.admin.service.SysMenuService;
import com.htstar.ovms.admin.service.SysRoleMenuService;
import com.htstar.ovms.admin.service.SysRoleService;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author ovms
 * @since 2017-10-29
 */
@Service
@AllArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
	private final CacheManager cacheManager;
	private final SysRoleService sysRoleService;
	private final SysRoleService roleService;
	private final SysMenuService menuService;
	/**
	 * @param role
	 * @param roleId  角色
	 * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.MENU_DETAILS, key = "#roleId + '_menu'")
	public Boolean saveRoleMenus(String role, Integer roleId, String menuIds,Integer etpId) {
		this.remove(Wrappers.<SysRoleMenu>query().lambda()
				.eq(SysRoleMenu::getRoleId, roleId));
		OvmsUser user = SecurityUtils.getUser();
		if (StrUtil.isBlank(menuIds)) {
			return Boolean.TRUE;
		}
//		int[] ints = Arrays.stream(menuIds.split(",")).mapToInt(s -> Integer.parseInt(s)).toArray();
//		List<SysMenu> menuLists = menuService.queryEtpMenuListsEtpId(ints);
//			for (SysRole sysRole : sysRoleService.getEtpRole(etpId)) {
//
//				saveRole(user.getEtpId(), sysRole.getEtpId(),sysRole.getRoleId(),menuLists);
//			}

		List<SysRoleMenu>  roleMenuList = Arrays
				.stream(menuIds.split(","))
				.map(menuId -> {
					SysRoleMenu roleMenu = new SysRoleMenu();
					roleMenu.setRoleId(roleId);
					roleMenu.setMenuId(Integer.valueOf(menuId));
					return roleMenu;
				}).collect(Collectors.toList());

		//清空userinfo
		cacheManager.getCache(CacheConstants.USER_DETAILS).clear();
		return this.saveBatch(roleMenuList);
	}
	@Transactional(rollbackFor = Exception.class)
	public void  saveRole(Integer etpId,Integer etpIds,Integer roleId,List<SysMenu> menuLists ){
		OvmsUser user = SecurityUtils.getUser();
//		// 保证插入租户为新的租户
		TenantContextHolder.setEtpId(etpIds);
		//通过当前企业查询
		 menuService.remove(Wrappers.<SysMenu>query()
				.eq("etp_id", etpIds));
		//List<SysMenu> menuListst = new ArrayList<>();
//		for (Integer in : ints) {
//			for (SysMenu sysMenu : menuLists) {
//				if(in.intValue() == sysMenu.getMenuId().intValue()){
//					sysMenu.setMenuId(sysMenu.getMenuId());
//					menuListst.add(sysMenu);
//				}
//			}
//		}    // 插入新的菜单

		saveTenantMenu(TreeUtil.buildTree(menuLists, CommonConstants.MENU_TREE_ROOT_ID), CommonConstants.MENU_TREE_ROOT_ID);
		List<SysMenu> newMenuList = menuService.list();
		// 查询全部菜单,构造角色菜单关系
	   this.remove(Wrappers.<SysRoleMenu>query()
				.lambda().eq(SysRoleMenu::getRoleId, roleId));
		List<SysRoleMenu> collect = newMenuList.stream().map(menu -> {
			SysRoleMenu roleMenu = new SysRoleMenu();
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(menu.getMenuId());
			return roleMenu;
		}).collect(Collectors.toList());
          this.saveBatch(collect);
	}
	/**
	 * 保存新的租户菜单，维护成新的菜单
	 *
	 * @param nodeList 节点树
	 * @param parent   上级
	 */
	private void saveTenantMenu(List<MenuTree> nodeList, Integer parent) {
		for (MenuTree node : nodeList) {
			SysMenu menu = new SysMenu();
			BeanUtils.copyProperties(node, menu, "parentId");
			menu.setParentId(parent);
			menuService.save(menu);
			if (CollUtil.isNotEmpty(node.getChildren())) {
				List<MenuTree> childrenList = node.getChildren().stream()
						.map(treeNode -> (MenuTree) treeNode).collect(Collectors.toList());
				saveTenantMenu(childrenList, menu.getMenuId());
			}
		}
	}
}
