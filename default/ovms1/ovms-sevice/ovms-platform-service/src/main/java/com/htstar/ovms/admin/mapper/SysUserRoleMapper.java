package com.htstar.ovms.admin.mapper;


import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.admin.api.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author ovms
 * @since 2017-10-29
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
	/**
	 * 根据用户Id删除该用户的角色关系
	 * @param userId 用户ID
	 * @return boolean
	 */
	Boolean deleteByUserId(@Param("userId") Integer userId);

	/**
	 * Description: 获取企业当前所有有效的管理员的ID
	 * Author: flr
	 * Date: 2020/7/1 15:05
	 * Company: 航通星空
	 * Modified By:
	 */
	@SqlParser(filter = true)
    List<Integer> queryAdminIdList(@Param("etpId") Integer etpId,@Param("roleCode") String roleCode);


}
