package com.htstar.ovms.enterprise.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.entity.CardCostInfo;
import com.htstar.ovms.enterprise.api.req.CardCostInfoReq;
import com.htstar.ovms.enterprise.mapper.CardCostInfoMapper;
import com.htstar.ovms.enterprise.service.CardCostInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 卡流水
 *
 * @author lw
 * @date 2020-07-22 14:25:29
 */
@Service
@Slf4j
public class CardCostInfoServiceImpl extends ServiceImpl<CardCostInfoMapper, CardCostInfo> implements CardCostInfoService {

    @Override
    public R saveInfo(CardCostInfo cardCostInfo) {
        OvmsUser user = SecurityUtils.getUser();
        Integer id = user.getId();
        Integer etpId = user.getEtpId();
        cardCostInfo.setEtpId(etpId);
        cardCostInfo.setUserId(id);
        this.save(cardCostInfo);
        return R.ok();
    }

    /**
     * 流水分页
     * @param req
     * @return
     */
    @Override
    public R<IPage<CardCostInfo>> queryPage(CardCostInfoReq req) {
        log.info("流水查询数据为{}", JSONObject.toJSONString(req));
        IPage<CardCostInfo> cardCostInfoIPage = baseMapper.queryPage(req);
        return  R.ok(cardCostInfoIPage);

    }
}
