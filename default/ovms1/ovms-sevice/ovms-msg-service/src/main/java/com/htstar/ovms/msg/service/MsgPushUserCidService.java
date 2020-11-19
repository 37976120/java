package com.htstar.ovms.msg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.msg.api.dto.MsgPushStyleDTO;

import com.htstar.ovms.msg.api.entity.MsgPushUserCid;
import com.htstar.ovms.msg.api.req.BatchPushByUserIdsReq;
import com.htstar.ovms.msg.api.req.BroadAppPushByEtpReq;
import com.htstar.ovms.msg.api.req.SingleAppPushReq;
import com.htstar.ovms.msg.api.vo.MsgPushUserCidVO;

import java.util.List;

/**
 * @author JinZhu
 * @Description: cid和用户绑定Mapper层
 * @date 2020/7/319:29
 */

public interface MsgPushUserCidService extends IService<MsgPushUserCid> {
    /**
     * 获取用户账号号进行推送
     *
     * @param cid
     * @return
     */
    MsgPushUserCidVO getMagPushUserCidByUserId(String cid, Integer userId);

    /**
     * 根据用户id修改cid
     *
     * @param cid
     * @return
     */
    int updateMagPushUserCidByUserId(String cid, Integer userId);

    /**
     * 根据用户id修改cid逻辑判断
     *
     * @param cid
     * @return
     */
    public R updateTosingledemo(String cid, Integer userId);

    public R toappdemo(MsgPushStyleDTO msgPushStyle);

    /**
     * 单推
     *
     * @param req
     * @return
     */
    R toSingdemo(SingleAppPushReq req);

    /**
     * 推送消息，向企业单推
     *
     * @param msgPushStyle
     * @return
     */
    R toSingdemos(BroadAppPushByEtpReq msgPushStyle);

    /**
     * Description: app 批次单点推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */
    R batchAppSinglePush(List<SingleAppPushReq> req);

    /**
     * Description: app 批量推送
     * Date: 2020/8/12 17:41
     * Company: 航通星空
     */

    R batchPushByUserIds(BatchPushByUserIdsReq req);


    /**
     * 自定义推送模板 向企业下面得管理员推送
     * @param  msgPushStyle
     */
     R EdpIdtoSingdemos(BroadAppPushByEtpReq msgPushStyle);

     R  batchAppSinglePushUncode(List<SingleAppPushReq> req);


     R  batchAppSinglePushReq(String req);

    /**
     * 用户注销清除该用户cid
     * @param userId
     * @return
     */
    void  deletePushByUserId(Integer userId);
}
