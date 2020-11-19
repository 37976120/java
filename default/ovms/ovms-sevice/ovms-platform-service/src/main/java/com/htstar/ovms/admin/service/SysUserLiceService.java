package com.htstar.ovms.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.admin.api.entity.SysUserLice;
import com.htstar.ovms.common.core.util.R;


/**
 * 用户驾驶证
 *
 * @author lw
 * @date 2020-07-08 10:32:23
 */
public interface SysUserLiceService extends IService<SysUserLice> {

    /**
     * 新增驾驶证
     * @param sysUserLice
     * @return
     */
    R saveInfo(SysUserLice sysUserLice);


    /**
     * 根据用户id获取
     * @param userId
     * @return
     */
    R<SysUserLice> getByUserId(Integer userId);
}
