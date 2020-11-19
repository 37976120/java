package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.EtpAdvert;
import com.htstar.ovms.enterprise.api.req.NoticeReq;

/**
 * 企业广告
 *
 * @author lw
 * @date 2020-08-10 17:29:10
 */
public interface EtpAdvertService extends IService<EtpAdvert> {

    /**
     * 新增广告
     * @param advert
     * @return
     */
    R saveEtpAdvert(EtpAdvert advert);

    /**
     * 删除企业广告
     * @param id
     * @return
     */
    R delEtpAdvert(Integer id);

    /**
     * 分页
     * @param req
     * @return
     */
    IPage<EtpAdvert> queryPage(NoticeReq req);


    /**
     * 根据id查询
     * @param id
     * @return
     */
    EtpAdvert getEtpAdvertById(Integer id);

    /**
     * 新增点击次数
     * @param id
     * @return
     */
    R addHits(Integer id);

    R test(Integer id);
}
