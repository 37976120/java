package com.htstar.ovms.admin.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.admin.api.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ovms
 * @since 2017-10-29
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
	/**
	 * 通过用户ID，查询角色信息
	 *
	 * @param userId
	 * @return
	 */
	List<SysRole> listRolesByUserId(Integer userId);

	/**
	 * Description: 通过roleCode获取角色ID
	 * Author: flr
	 * Company: 航通星空
	 */
	@SqlParser(filter = true)
	Integer getRoleIdByCode(@Param("roleCode") String roleCode, @Param("etpId") Integer etpId);
}
