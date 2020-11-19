package com.htstar.ovms.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.admin.api.dto.UserDTO;
import com.htstar.ovms.admin.api.dto.UserInfo;
import com.htstar.ovms.admin.api.entity.SysUser;
import com.htstar.ovms.admin.api.req.PageSimpleUserReq;
import com.htstar.ovms.admin.api.req.UpdateProfileReq;
import com.htstar.ovms.admin.api.req.UpdatePwdSmsReq;
import com.htstar.ovms.admin.api.vo.SimpleUserVO;
import com.htstar.ovms.admin.api.vo.UserMsgVO;
import com.htstar.ovms.admin.api.vo.UserVO;
import com.htstar.ovms.common.core.util.R;

import java.util.List;

/**
 * @author ovms
 * @date 2017/10/31
 */
public interface SysUserService extends IService<SysUser> {
	/**
	 * 查询用户信息
	 *
	 * @param sysUser 用户
	 * @return userInfo
	 */
	UserInfo findUserInfo(SysUser sysUser);

	/**
	 * 分页查询用户信息（含有角色信息）
	 *
	 * @param page    分页对象
	 * @param userDTO 参数列表
	 * @return
	 */
	IPage getUsersWithRolePage(Page page, UserDTO userDTO);

	/**
	 * 删除用户
	 *
	 * @param sysUser 用户
	 * @return boolean
	 */
	Boolean deleteUserById(SysUser sysUser);

	/**
	 * 更新当前用户基本信息
	 *
	 * @param userDto 用户信息
	 * @return Boolean
	 */
	R<Boolean> updateUserInfo(UserDTO userDto);

	/**
	 * 更新指定用户信息
	 *
	 * @param userDto 用户信息
	 * @return
	 */
	Boolean updateUser(UserDTO userDto);

	/**
	 * 通过ID查询用户信息
	 *
	 * @param id 用户ID
	 * @return 用户信息
	 */
	UserVO selectUserVoById(Integer id);

	/**
	 * 查询上级部门的用户信息
	 *
	 * @param username 用户名
	 * @return R
	 */
	List<SysUser> listAncestorUsers(String username);

	/**
	 * 保存用户信息
	 *
	 * @param userDto DTO 对象
	 * @return success/fail
	 */
	Boolean saveUser(UserDTO userDto);

    SysUser getUserForLogin(String username);

	/**
	 * 短信验证码修改用户密码
	 * @param req
	 * @return
	 */
	R updatePwdBySms(UpdatePwdSmsReq req);


    /**
     * 修改姓名
     * @param req
     * @return
     */
	R updateNickName(UpdateProfileReq req);


    /**
     * 修改性别
     * @param req
     * @return
     */
	R updateSex(UpdateProfileReq req);

    /**
     * 绑定邮箱
     * @param req
     * @return
     */
	R bindingEmail(UpdateProfileReq req);

	/**
	 * Description: 简化分页（常量方式）
	 * Author: flr
	 * Date: 2020/7/10 15:56
	 * Company: 航通星空
	 * Modified By:
	 */
    R<Page<SimpleUserVO>> simplePage(PageSimpleUserReq req);

    List<UserMsgVO> getUserMsgVOListByRoleId(Integer roleId);

	List<UserMsgVO> getUserMsgVOListByIds(List<Integer> userIdList);
}
