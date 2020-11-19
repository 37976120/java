package com.htstar.ovms.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.admin.api.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 *
 * @author ovms
 * @since 2017-11-20
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {
}
