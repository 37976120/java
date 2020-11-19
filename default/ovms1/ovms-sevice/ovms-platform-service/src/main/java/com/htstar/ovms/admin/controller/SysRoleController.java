package com.htstar.ovms.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.entity.SysMenu;
import com.htstar.ovms.admin.api.entity.SysRole;
import com.htstar.ovms.admin.api.vo.RoleVO;
import com.htstar.ovms.admin.service.SysMenuService;
import com.htstar.ovms.admin.service.SysRoleMenuService;
import com.htstar.ovms.admin.service.SysRoleService;
import com.htstar.ovms.admin.service.SysUserRoleService;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author ovms
 * @date 2020-02-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/role")
@Api(value = "role", tags = "角色管理模块")
public class SysRoleController {
	private final SysRoleService sysRoleService;
	private final SysRoleMenuService sysRoleMenuService;
	private final SysUserRoleService userRoleService;
	private final SysMenuService sysMenuService;

	@GetMapping("/queryAdminIdListInner/{etpId}")
	@Inner
	List<Integer> queryAdminIdListInner(@PathVariable("etpId")Integer etpId){
		return userRoleService.queryAdminIdList(etpId);
	}

	/**
	 * Description: 获取企业当前所有有效的管理员的ID
	 * @param etpId 企业ID 必填
	 * Author: flr
	 * Date: 2020/7/1 15:05
	 * Company: 航通星空
	 * Modified By:
	 */
	@GetMapping("/queryAdminIdList/{etpId}")
	List<Integer> queryAdminIdList(@PathVariable("etpId")Integer etpId){
		return userRoleService.queryAdminIdList(etpId);
	}

	/**
	 * Description: 给用户赋予司机角色
	 * @param userId 用户ID 必填
	 * @param etpId 企业ID 必填
	 * Author: flr
	 * Company: 航通星空
	 * Modified By:
	 */
	@Inner
	@GetMapping("/saveUserDriver/{userId}/{etpId}")
	public R saveUserDriver(@PathVariable("userId") Integer userId,@PathVariable("etpId") Integer etpId) {
		return R.ok(userRoleService.saveOneRoleByCode(CommonConstants.ROLE_DRIVER,userId,etpId));
	}

    @Inner
	@GetMapping("/removeUserDriver/{userId}/{etpId}")
    public R removeUserDriver(@PathVariable("userId") Integer userId,@PathVariable("etpId") Integer etpId) {
        return R.ok(userRoleService.removeDriver(CommonConstants.ROLE_DRIVER,userId,etpId));
    }
	/**
	 * 通过ID查询角色信息
	 *
	 * @param id ID
	 * @return 角色信息
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable Integer id) {
		return R.ok(sysRoleService.getById(id));
	}

	/**
	 * 添加角色
	 *
	 * @param sysRole 角色信息
	 * @return success、false
	 */
	@SysLog("添加角色")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_role_add')")
	public R save(@Valid @RequestBody SysRole sysRole) {
		return R.ok(sysRoleService.save(sysRole));
	}

	/**
	 * 修改角色
	 *
	 * @param sysRole 角色信息
	 * @return success/false
	 */
	@SysLog("修改角色")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_role_edit')")
	public R update(@Valid @RequestBody SysRole sysRole) {
		return sysRoleService.updateRole(sysRole);
	}

	/**
	 * 删除角色
	 *
	 * @param id
	 * @return
	 */
	@SysLog("删除角色")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_role_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(sysRoleService.removeRoleById(id));
	}

	/**
	 * 获取角色列表
	 *
	 * @return 角色列表
	 */
	@GetMapping("/list")
	public R listRoles() {
		return R.ok(sysRoleService.list(Wrappers.emptyWrapper()));
	}

	/**
	 * 分页查询角色信息
	 *
	 * @param page 分页对象
	 * @return 分页对象
	 */
	@GetMapping("/page")
	public R getRolePage(Page page) {
		return R.ok(sysRoleService.page(page, Wrappers.emptyWrapper()));
	}

	/**
	 * 更新角色菜单
	 *
	 * @param roleVo 角色对象
	 * @return success、false
	 */
	@SysLog("更新角色菜单")
	@PutMapping("/menu")
	@PreAuthorize("@pms.hasPermission('sys_role_perm')")
	public R saveRoleMenus(@RequestBody RoleVO roleVo) {
		SysRole sysRole = sysRoleService.getById(roleVo.getRoleId());
		boolean falg = false;
		if(Objects.equals(sysRole.getRoleCode(),CommonConstants.ROLE_ADMIN)){
			int[] ints = Arrays.stream(roleVo.getMenuIds().split(",")).mapToInt(s -> Integer.parseInt(s)).toArray();
			for (SysMenu sysMenu : sysMenuService.getMenuIdName(ints)) {
				if(Objects.equals(sysMenu.getName(),"权限管理")){
					falg=true;
					continue;
				}
			}
			if(!falg){
					return R.failed("管理员权限管理必须勾选");
			}
		}
		return R.ok(sysRoleMenuService.saveRoleMenus(sysRole.getRoleCode()
				, roleVo.getRoleId(), roleVo.getMenuIds(),sysRole.getEtpId()));
	}

	/**
	 * 通过角色ID 查询角色列表
	 *
	 * @param roleIdList 角色ID
	 * @return
	 */
	@PostMapping("/getRoleList")
	public R getRoleList(@RequestBody List<String> roleIdList) {
		return R.ok(sysRoleService.listByIds(roleIdList));
	}
}
