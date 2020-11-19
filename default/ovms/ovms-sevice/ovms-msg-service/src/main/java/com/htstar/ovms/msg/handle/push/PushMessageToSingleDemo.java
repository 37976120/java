package com.htstar.ovms.msg.handle.push;


import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.template.*;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.msg.api.constant.TargetTypeConstant;
import com.htstar.ovms.msg.api.dto.MsgPushStyleDTO;;
import com.htstar.ovms.msg.handle.template.PushTemplate;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


import static com.htstar.ovms.msg.handle.appinfo.AppInfo.push;


/**
 * 单推和批量单推相关demo
 *
 * @author zhangwf
 * @see
 * @since 2019-07-09
 */
@Slf4j
public class PushMessageToSingleDemo {

//    public static void main(String[] args) {
//        System.setProperty("needOSAsigned", "true");
//        // 返回别名对应的每个cid的推送详情
//        System.setProperty(Constants.GEXIN_PUSH_SINGLE_ALIAS_DETAIL, "true");
//        //pushToSingle();
////        pushToSingleBatch();
//    }

    /**
     * 对单个用户推送消息
     * <p>
     * 场景1：某用户发生了一笔交易，银行及时下发一条推送消息给该用户。
     * <p>
     * 场景2：用户定制了某本书的预订更新，当本书有更新时，需要向该用户及时下发一条更新提醒信息。
     * 这些需要向指定某个用户推送消息的场景，即需要使用对单个用户推送消息的接口。
     */
    public static R pushToSingle(MsgPushStyleDTO msgPushStyle, String userName, String cid ) {
        TransmissionTemplate template = PushTemplate.getTransmissionTemplate(msgPushStyle);
        // 单推消息类型
        SingleMessage message = getSingleMessage(template);
        Target target = new Target();
        target.setAppId(TargetTypeConstant.APP_ID);
        target.setClientId(cid);//用户唯一标识cid
        target.setAlias(userName); //别名需要提前绑定
        push.bindAlias(target.getAppId(),target.getAlias(),target.getClientId());
        IAliasResult x = push.queryClientId(TargetTypeConstant.APP_ID, target.getAlias());
        IPushResult ret = null;
        try{
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        try {
            if (ret != null) {
                log.info(ret.getResponse().get("taskId").toString());
            } else {

            }
        } catch (Exception e) {
             log.error("对该cid没有操作权限");
        }

        return R.ok(ret.getResponse().toString(),x.getResponse().toString());
    }



    /**
     * 批量单推
     * <p>
     * 当单推任务较多时，推荐使用该接口，可以减少与服务端的交互次数。
     */
    public static R pushToSingleBatch(MsgPushStyleDTO msgPushStyle,String userName,String cid) {
        IBatch batch = push.getBatch();

        IPushResult ret = null;
        try {
            //构建客户a的透传消息a
            constructClientTransMsg(msgPushStyle,cid,batch);
            //构建客户B的点击通知打开网页消息b
            constructClientLinkMsg(msgPushStyle,cid, batch);
            ret = batch.submit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ret = batch.retry();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }
        return R.ok(ret.getResponse().toString());
    }

    private static void constructClientTransMsg(MsgPushStyleDTO msgPushStyle,String cid, IBatch batch) throws Exception {
        NotificationTemplate template = PushTemplate.getNotificationTemplate(msgPushStyle);
        SingleMessage message = getSingleMessage(template);

        // 设置推送目标，填入appid和clientId
        Target target = new Target();
        target.setAppId(TargetTypeConstant.APP_ID);
        target.setClientId(cid);
        batch.add(message, target);
    }

    private static void constructClientLinkMsg(MsgPushStyleDTO msgPushStyle,String cid, IBatch batch) throws Exception {
        AbstractTemplate template = PushTemplate.getNotificationTemplate(msgPushStyle);
        SingleMessage message = getSingleMessage(template);

        // 设置推送目标，填入appid和clientId
        Target target = new Target();
        target.setAppId(TargetTypeConstant.APP_ID);
        target.setClientId(cid);
        batch.add(message, target);
    }

    private static SingleMessage getSingleMessage(AbstractTemplate template) {
        SingleMessage message = new SingleMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(1 * 3600 * 1000);
        // 判断客户端是否wifi环境下推送。1为仅在wifi环境下推送，0为不限制网络环境，默认不限
        message.setPushNetWorkType(0);// 判断客户端是否wifi环境下推送。1为仅在wifi环境下推送，0为不限制网络环境，默认不限
        // 厂商下发策略；1: 个推通道优先，在线经个推通道下发，离线经厂商下发(默认);2: 在离线只经厂商下发;3: 在离线只经个推通道下发;4: 优先经厂商下发，失败后经个推通道下发;
        //message.setStrategyJson("{\"default\":4,\"ios\":4,\"st\":4}");
        message.setStrategyJson("{\"default\":1,\"ios\":2}");
        return message;
    }

}
