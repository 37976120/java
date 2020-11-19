package com.htstar.ovms.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.dto.UserDTO;
import com.htstar.ovms.admin.api.dto.UserInfo;
import com.htstar.ovms.admin.api.entity.EtpInfo;
import com.htstar.ovms.admin.api.entity.SysUser;
import com.htstar.ovms.admin.api.entity.SysUserLice;
import com.htstar.ovms.admin.api.req.PageSimpleUserReq;
import com.htstar.ovms.admin.api.req.UpdateProfileReq;
import com.htstar.ovms.admin.api.req.UpdatePwdSmsReq;
import com.htstar.ovms.admin.api.vo.SimpleUserVO;
import com.htstar.ovms.admin.api.vo.UserMsgVO;
import com.htstar.ovms.admin.api.vo.UserVO;
import com.htstar.ovms.admin.service.EtpInfoService;
import com.htstar.ovms.admin.service.SysUserLiceService;
import com.htstar.ovms.admin.service.SysUserService;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * @author ovms
 * @date 2018/12/16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Api(value = "user", tags = "用户管理模块")
public class SysUserController {
	@Autowired
	private SysUserService userService;
    @Autowired
    private SysUserLiceService sysUserLiceService;
    @Autowired
	private EtpInfoService etpInfoService;

	@ApiOperation(value = "短信验证码修改用户密码", notes = "短信验证码修改用户密码")
	@PostMapping("/updatePwdBySms")
	public R updatePwdBySms(@RequestBody UpdatePwdSmsReq req) {
		return userService.updatePwdBySms(req);
	}


	/**
	 * 获取指定用户全部信息
	 * @return 用户信息
	 */
	@ApiIgnore
	@Inner
	@GetMapping("/info/{username}")
	public R<UserInfo> info(@PathVariable String username) {
		SysUser user = userService.getUserForLogin(username);
		if (user == null) {
			return R.failed(null, String.format("用户不存在 %s", username));
		}
		if (user.getLockFlag().equals(CommonConstants.STATUS_LOCK)){
			return R.failed("您的账号已被冻结！");
		}
		EtpInfo etp = etpInfoService.getById(user.getEtpId());
		if (null == etp){
			return R.failed("您还未加入企业或企业已被删除");
		}

		if (etp.getDelFlag() == 1){
			return R.failed("您所在的企业已被删除！");
		}
		LocalDate now = OvmDateUtil.getCstNowDate();
		if (now.isAfter(etp.getEndTime())){
			return R.failed("您所在企业对平台的使用时间已过期");
		}

		if (etp.getEtpStatus().equals(CommonConstants.STATUS_LOCK)){
			return R.failed("您所在企业企业已被冻结！");
		}
		return R.ok(userService.findUserInfo(user));
	}

	/**
	 * 通过ID查询用户信息
	 *
	 * @param id ID
	 * @return 用户信息
	 */
    @ApiOperation(value = "通过ID查询用户信息", notes = "通过ID查询用户信息")
	@GetMapping("/{id}")
	public R<UserVO> user(@PathVariable Integer id) {
		return R.ok(userService.selectUserVoById(id));
	}

	/**
	 * 根据用户名查询用户信息
	 *
	 * @param username 用户名
	 * @return
	 */
	@GetMapping("/details/{username}")
	@ApiOperation(value = "根据手机号查询用户信息", notes = "根据用户名查询用户信息")
	public R user(@PathVariable String username) {
		SysUser condition = new SysUser();
		condition.setUsername(username);
		return R.ok(userService.getOne(new QueryWrapper<>(condition)));
	}

	/**
	 * 删除用户信息
	 *
	 * @param id ID
	 * @return R
	 */
	@SysLog("删除用户信息")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_user_del')")
	@ApiOperation(value = "删除用户", notes = "根据ID删除用户")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path")
	public R userDel(@PathVariable Integer id) {
		SysUser sysUser = userService.getById(id);
		return R.ok(userService.deleteUserById(sysUser));
	}

	/**
	 * 添加用户
	 *
	 * @param userDto 用户信息
	 * @return success/false
	 */
	@SysLog("添加用户")
	@PostMapping
	@ApiOperation(value = "新增用户", notes = "平台新增用户")
	@PreAuthorize("@pms.hasPermission('sys_user_add')")
	public R user(@RequestBody UserDTO userDto) {
		if(StrUtil.isBlank(userDto.getPassword())){
			return R.failed("填个密码！");
		}
		return R.ok(userService.saveUser(userDto));
	}

	/**
	 * 更新用户信息
	 *
	 * @param userDto 用户信息
	 * @return R
	 */
	@SysLog("更新用户信息")
	@ApiOperation(value = "更新用户信息", notes = "平台更新用户信息")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_user_edit')")
	public R updateUser(@Valid @RequestBody UserDTO userDto) {
		return R.ok(userService.updateUser(userDto));
	}

	/**
	 * 分页查询用户
	 *
	 * @param page    参数集
	 * @param userDTO 查询参数列表
	 * @return 用户集合
	 */
	@GetMapping("/page")
	@ApiOperation(value = "分页查询用户", notes = "分页查询用户")
	public R getUserPage(Page page, UserDTO userDTO) {
		return R.ok(userService.getUsersWithRolePage(page, userDTO));
	}


	/**
	 * Description: 简化分页（常量方式）
	 * Author: flr
	 * Date: 2020/7/10 15:56
	 * Company: 航通星空
	 * Modified By:
	 */
	@PostMapping("/simplePage")
	@ApiOperation(value = "简化分页（常量方式）", notes = "简化分页")
	public R<Page<SimpleUserVO>> simplePage(@RequestBody PageSimpleUserReq req) {
		return userService.simplePage(req);
	}

	/**
	 * 修改个人信息
	 *
	 * @param userDto userDto
	 * @return success/false
	 */
	@SysLog("修改个人信息")
	@PutMapping("/edit")
	@ApiOperation(value = "修改个人信息", notes = "修改个人信息")
	public R updateUserInfo(@Valid @RequestBody UserDTO userDto) {
		return userService.updateUserInfo(userDto);
	}

	/**
	 * @param username 用户名称
	 * @return 上级部门用户列表
	 */
	@GetMapping("/ancestor/{username}")
	@ApiOperation(value = "上级部门用户列表", notes = "上级部门用户列表")
	public R listAncestorUsers(@PathVariable String username) {
		return R.ok(userService.listAncestorUsers(username));
	}

    @ApiOperation(value = "修改用户姓名", notes = "修改用户姓名")
    @PostMapping("/updateNickName")
    public R updateNickName(@RequestBody UpdateProfileReq req) {
        return userService.updateNickName(req);
    }

    @ApiOperation(value = "修改用户性别", notes = "修改用户性别")
    @PostMapping("/updateSex")
    public R updateSex(@RequestBody UpdateProfileReq req) {
       return userService.updateSex(req);
    }
    @ApiOperation(value = "绑定邮箱", notes = "绑定邮箱")
    @PostMapping("/bindingEmail")
    public R bindingEmail(@RequestBody UpdateProfileReq req) {
        return userService.bindingEmail(req);
    }
    @ApiOperation(value = "添加驾驶证", notes = "添加驾驶证")
    @PostMapping("/saveLice")
    public R saveUserLice(@RequestBody SysUserLice sysUserLice){
	    return sysUserLiceService.saveInfo(sysUserLice);
    }

    @ApiOperation(value = "获取驾驶证信息", notes = "获取驾驶证信息")
    @GetMapping("/getLice/{userId}")
    public R getUserLice(@PathVariable("userId") Integer userId){
        return sysUserLiceService.getByUserId(userId);
    }


	@ApiIgnore
	@Inner
	@GetMapping("/getUserMsgVOListByRoleId/{roleId}")
	public List<UserMsgVO> getUserMsgVOListByRoleId(@PathVariable("roleId") Integer roleId){
		return userService.getUserMsgVOListByRoleId(roleId);
	}

	@ApiIgnore
	@Inner
	@PostMapping("/getUserMsgVOListByIds")
	public List<UserMsgVO> getUserMsgVOListByIds(@RequestBody List<Integer> userIdList){
		return userService.getUserMsgVOListByIds(userIdList);
	}
}
