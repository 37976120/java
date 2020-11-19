package com.htstar.ovms.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.admin.api.entity.SysUserApply;
import com.htstar.ovms.admin.api.req.AppCreateEtpReq;
import com.htstar.ovms.admin.api.req.ApplyJoinReq;
import com.htstar.ovms.admin.api.req.ApprovalJoinReq;
import com.htstar.ovms.admin.api.req.SysUserApplyPageReq;
import com.htstar.ovms.common.core.util.R;

/**
 * 企业员工申请加入
 *
 * @author flr
 * @date 2020-06-29 10:18:39
 */
public interface SysUserApplyService extends IService<SysUserApply> {

    /**
     * 申请加入企业
     * @param req
     * @return
     */
    R applyJoin(ApplyJoinReq req);

    /**
     * 审批员工加入申请
     * @param req
     * @return
     */
    R approval(ApprovalJoinReq req);

    R getSysUserApplyPage(SysUserApplyPageReq req);
}
