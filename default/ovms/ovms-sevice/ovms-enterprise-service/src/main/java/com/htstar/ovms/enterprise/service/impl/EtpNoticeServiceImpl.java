package com.htstar.ovms.enterprise.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.entity.EtpNotice;
import com.htstar.ovms.enterprise.api.req.NoticeReq;
import com.htstar.ovms.enterprise.mapper.EtpNoticeMapper;
import com.htstar.ovms.enterprise.service.EtpNoticeService;
import com.htstar.ovms.msg.api.feign.MsgAppPushFeign;
import com.htstar.ovms.msg.api.req.BroadAppPushByEtpReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 企业公告
 *
 * @author lw
 * @date 2020-08-10 11:38:31
 */
@Service
@Slf4j
public class EtpNoticeServiceImpl extends ServiceImpl<EtpNoticeMapper, EtpNotice> implements EtpNoticeService {
@Autowired
private MsgAppPushFeign msgAppPushFeign;
    @Override
    public R saveNotice(EtpNotice notice) {
        OvmsUser user = SecurityUtils.getUser();
        Integer etpId = user.getEtpId();
        Integer userId = user.getId();
        notice.setPutUserId(userId);
        notice.setEtpId(etpId);
        this.save(notice);
        //推送
        BroadAppPushByEtpReq req = new BroadAppPushByEtpReq();
        req.setEtpId(etpId);
        req.setTitle(notice.getNoticeTitle());
        req.setContent(notice.getNoticeContent());
        req.setMsgType(2);
        msgAppPushFeign.broadAppPushByEtp(req, SecurityConstants.FROM_IN);
        return R.ok();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public R delNotice(Integer id) {
        baseMapper.delNotice(id);
        return R.ok("删除成功");
    }

    /**
     * 分页
     * @param req
     * @return
     */
    @Override
    public R<IPage<EtpNotice>> queryPage(NoticeReq req) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        if (req.getEtpId()==null&&etpId!=1){
            req.setEtpId(etpId);
        }
        return  R.ok(baseMapper.queryPage(req));
    }

}
