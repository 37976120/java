package com.htstar.ovms.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.entity.SysUserApply;
import com.htstar.ovms.admin.api.req.SysUserApplyPageReq;
import com.htstar.ovms.admin.api.vo.SysUserApplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 企业员工申请加入
 *
 * @author flr
 * @date 2020-06-29 10:18:39
 */
@Mapper
public interface SysUserApplyMapper extends BaseMapper<SysUserApply> {

    SysUserApply getApplyByUsername(@Param("username") String username);

    IPage<SysUserApplyVO> getSysUserApplyPage(SysUserApplyPageReq req);
}
