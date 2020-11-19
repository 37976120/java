package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.UnicodeUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.enterprise.api.vo.ItemPushVO;
import com.htstar.ovms.enterprise.mapper.ItemPushMapper;
import com.htstar.ovms.enterprise.service.ItemExpirePushService;
import com.htstar.ovms.msg.api.feign.MsgAppPushFeign;
import com.htstar.ovms.msg.api.req.SingleAppPushReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/20
 * Company: 航通星空
 * Modified By:
 */
@Service
@Slf4j
public class ItemExpirePushServiceImpl extends ServiceImpl<ItemPushMapper, T> implements ItemExpirePushService {
    @Autowired
    private MsgAppPushFeign msgAppPushFeign;
    /**
     * 年检到期推送
     * @return
     */
    @Override
    public R getMotItemExpirePush() {
        log.info("年检到期推送---------------");
        List<ItemPushVO> motItemPushList = baseMapper.getMotItemExpirePush();
        List<SingleAppPushReq> pushList =new ArrayList<>();
        if(CollUtil.isNotEmpty(motItemPushList)){
            for (ItemPushVO itemPushVO : motItemPushList) {
                SingleAppPushReq singleAppPushReq = new SingleAppPushReq();
                singleAppPushReq.setTitle("年检到期提醒");
                singleAppPushReq.setMsgType(3);
                singleAppPushReq.setContent("车牌号"+itemPushVO.getLicCode()+"的年检还剩"+itemPushVO.getTimeValue()+"到期,请及时处理");
                singleAppPushReq.setUserId(itemPushVO.getUserId());
                pushList.add(singleAppPushReq);
            }
        }
        msgAppPushFeign.batchAppSinglePush(pushList, SecurityConstants.FROM_IN);
        return R.ok(pushList);
    }

    /**
     * 保险过期提醒
     * @return
     */
    @Override
    public R getInsItemExpirePush() {
        List<ItemPushVO> insItemPushList = baseMapper.getInsItemExpirePush();
        log.info("保险到期推送--------------");
        List<SingleAppPushReq> pushList =new ArrayList<>();
        if(CollUtil.isNotEmpty(insItemPushList)){
            for (ItemPushVO itemPushVO : insItemPushList) {
                SingleAppPushReq singleAppPushReq = new SingleAppPushReq();
                singleAppPushReq.setTitle("保险到期提醒");
                singleAppPushReq.setMsgType(3);
                //保险类型
                Integer insType=itemPushVO.getInsType();
                String type="";
                if (insType==0){
                    type="交强险";
                }else if (insType==1){
                    type="商业险";
                }
                else if (insType==2){
                    type="货物保险";
                }
                else if (insType==3){
                    type="司机保险";
                }

                singleAppPushReq.setContent("车牌号"+itemPushVO.getLicCode()+"的"+type+"还剩"+itemPushVO.getTimeValue()+"到期,请及时处理");
                singleAppPushReq.setUserId(itemPushVO.getUserId());
                pushList.add(singleAppPushReq);
            }
        }
        msgAppPushFeign.batchAppSinglePush(pushList, SecurityConstants.FROM_IN);
        return R.ok(pushList);
    }
}
