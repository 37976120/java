package com.htstar.ovms.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.admin.api.entity.SysTenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户
 *
 * @author ovms
 * @date 2019-05-15 15:55:41
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {

}
