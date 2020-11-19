package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.EtpNotice;
import com.htstar.ovms.enterprise.api.req.NoticeReq;

/**
 * 企业公告
 *
 * @author lw
 * @date 2020-08-10 11:38:31
 */
public interface EtpNoticeService extends IService<EtpNotice> {

    /**
     * 新增公告
     * @param notice
     * @return
     */
    R saveNotice(EtpNotice notice);

    /**
     * 删除公告
     * @param id
     * @return
     */
    R delNotice(Integer id);

    /**
     * 分页
     * @param req
     * @return
     */
    R<IPage<EtpNotice>> queryPage(NoticeReq req);

}
