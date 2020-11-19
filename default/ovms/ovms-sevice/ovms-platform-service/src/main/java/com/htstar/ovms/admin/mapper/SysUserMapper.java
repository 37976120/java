package com.htstar.ovms.admin.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.dto.UserDTO;
import com.htstar.ovms.admin.api.entity.SysUser;
import com.htstar.ovms.admin.api.req.PageSimpleUserReq;
import com.htstar.ovms.admin.api.vo.SimpleUserVO;
import com.htstar.ovms.admin.api.vo.UserMsgVO;
import com.htstar.ovms.admin.api.vo.UserVO;
import com.htstar.ovms.common.data.datascope.DataScope;
import com.htstar.ovms.common.data.datascope.DataScopeMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author ovms
 * @since 2017-10-29
 */
@Mapper
public interface SysUserMapper extends DataScopeMapper<SysUser> {
	/**
	 * 通过用户名查询用户信息（含有角色信息）
	 *
	 * @param username 用户名
	 * @return userVo
	 */
	UserVO getUserVoByUsername(String username);

	/**
	 * 分页查询用户信息（含角色）
	 *
	 * @param page      分页
	 * @param userDTO   查询参数
	 * @param dataScope
	 * @return list
	 */
	IPage<List<UserVO>> getUserVosPage(Page page, @Param("query") UserDTO userDTO);

	/**
	 * 通过ID查询用户信息
	 *
	 * @param id 用户ID
	 * @return userVo
	 */
	UserVO getUserVoById(Integer id);

	@SqlParser(filter = true)
	SysUser getUserForLogin(String username);
	/**
	 * Description: 简化分页（常量方式）
	 * Author: flr
	 * Date: 2020/7/10 15:56
	 * Company: 航通星空
	 * Modified By:
	 */
	@SqlParser(filter = true)
    Page<SimpleUserVO> simplePage(PageSimpleUserReq req);

	@SqlParser(filter = true)
	Map<String,String> getApplyVerifyUserStr(@Param("etpId") Integer etpId, @Param("applyType") Integer applyType);

	@SqlParser(filter = true)
    List<UserMsgVO> getUserMsgVOListByRoleId(@Param("roleId") Integer roleId);

	@SqlParser(filter = true)
	List<UserMsgVO> getUserMsgVOListByIds(@Param("userIds") String userIds);
}
