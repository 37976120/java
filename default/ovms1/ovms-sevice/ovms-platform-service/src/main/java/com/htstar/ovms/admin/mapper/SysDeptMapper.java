package com.htstar.ovms.admin.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.htstar.ovms.admin.api.dto.DeptTree;
import com.htstar.ovms.admin.api.entity.SysDept;
import com.htstar.ovms.common.data.datascope.DataScopeMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 部门管理 Mapper 接口
 * </p>
 *
 * @author ovms
 * @since 2018-01-20
 */
@Mapper
public interface SysDeptMapper extends DataScopeMapper<SysDept> {

    @SqlParser(filter = true)
    List<DeptTree> selectTreeByEtpId(@Param("etpId") Integer etpId);

    /**
     * @Author Hanguji
     * @param etpId
     * @return
     */
    @SqlParser(filter = true)
    List<SysDept> listDeptsByEtpId(@Param("etpId") Integer etpId);
}
