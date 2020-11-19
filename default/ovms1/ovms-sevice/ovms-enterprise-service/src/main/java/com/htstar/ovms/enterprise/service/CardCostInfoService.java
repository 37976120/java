package com.htstar.ovms.enterprise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.entity.CardCostInfo;
import com.htstar.ovms.enterprise.api.req.CardCostInfoReq;


/**
 * 卡流水
 *
 * @author lw
 * @date 2020-07-22 14:25:29
 */
public interface CardCostInfoService extends IService<CardCostInfo> {
    /**
     * 添加流水
     * @param cardCostInfo
     * @return
     */
    R saveInfo(CardCostInfo cardCostInfo);

    /**
     * 流水明细
     * @param req
     * @return
     */
    R<IPage<CardCostInfo>> queryPage(CardCostInfoReq req);
}
