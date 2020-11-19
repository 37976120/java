package com.htstar.ovms.msg.handle.template;

import com.alibaba.fastjson.JSONObject;


import com.gexin.rp.sdk.base.notify.Notify;
import com.gexin.rp.sdk.base.payload.APNPayload;

import com.gexin.rp.sdk.base.payload.VoIPPayload;
import com.gexin.rp.sdk.template.*;
import com.htstar.ovms.msg.api.constant.TargetTypeConstant;
import com.htstar.ovms.msg.api.dto.MsgPushStyleDTO;

import lombok.extern.slf4j.Slf4j;


import java.util.HashMap;

import java.util.Map;


/**
 * 推送模板
 *
 * @author JinZhu
 * @Description:
 * @date 2020/7/2810:09
 */
@Slf4j
public class PushTemplate {


    /**
     * 点击通知打开应用模板, 在通知栏显示一条含图标、标题等的通知，用户点击后激活您的应用。
     * 通知模板(点击后续行为: 支持打开应用、发送透传内容、打开应用同时接收到透传 这三种行为)
     *
     * @return
     */
    public static NotificationTemplate getNotificationTemplate(MsgPushStyleDTO msgPushStyle) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY

        template.setAppId(TargetTypeConstant.APP_ID);
        template.setAppkey(TargetTypeConstant.APP_KEY);
        template.setTransmissionType(1);  // 透传消息设置，收到消息是否立即启动应用： 1为立即启动，2则广播等待客户端自启动
        template.setTransmissionContent("我是普通消息通知1");
//        template.setSmsInfo(PushSmsInfo.getSmsInfo()); //短信补量推送

//        template.setDuration("2019-07-09 11:40:00", "2019-07-09 12:24:00");  // 设置定时展示时间，安卓机型可用
//        template.setNotifyid(123); // 在消息推送的时候设置notifyid。如果需要覆盖此条消息，则下次使用相同的notifyid发一条新的消息。客户端sdk会根据notifyid进行覆盖。
        //设置展示样式
        //getTransmissionTemplate(msgPushStyle);
        template.setStyle(PushStyle.getStyle0(msgPushStyle)); //安卓消息推送
        template.setAPNInfo(getAPNPayload(msgPushStyle)); //ios消息推送
        return template;
    }

    /**
     * 点击通知打开(第三方)网页模板, 在通知栏显示一条含图标、标题等的通知，用户点击可打开您指定的网页。
     *
     * @return
     */
    public static LinkTemplate getLinkTemplate(MsgPushStyleDTO msgPushStyle) {
        LinkTemplate template = new LinkTemplate();
        // 设置APPID与APPKEY
        template.setAppId(TargetTypeConstant.APP_ID);
        template.setAppkey(TargetTypeConstant.APP_KEY);
        //设置展示样式
        template.setStyle(PushStyle.getStyle0(msgPushStyle));
        template.setUrl("http://www.baidu.com");  // 设置打开的网址地址

        template.setStyle(PushStyle.getStyle0(msgPushStyle)); //安卓消息推送
        template.setAPNInfo(getAPNPayload(msgPushStyle)); //ios消息推送

        return template;
    }

    /**
     * 透传消息模版,透传消息是指消息传递到客户端只有消息内容，
     * 展现形式由客户端自行定义。客户端可自定义通知的展现形式，
     * 也可自定义通知到达之后的动作，或者不做任何展现。
     * @return
     */
    public static TransmissionTemplate getTransmissionTemplate(MsgPushStyleDTO msgPushStyle) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setTransmissionType(2);
        Map<String, Object> customParam1 = new HashMap<>();
        if(msgPushStyle.getMsgType() == 1){ //用车通知地址
            customParam1.put("payload","/pages/usecar/usecar-detail?id="+msgPushStyle.getId());
        }
        if(msgPushStyle.getMsgType() == 2){//公告通知地址
            customParam1.put("payload","/pages/publics/publics-detail?id="+msgPushStyle.getId());
        }
        if(msgPushStyle.getMsgType() == 3){//消息提醒地址
            customParam1.put("payload","/pages/caution/caution-detail?id="+msgPushStyle.getId());
        }
        customParam1.put("sound","default");
        customParam1.put("title",msgPushStyle.getTitle());
        customParam1.put("content",msgPushStyle.getContent());
        // 设置APPID与APPKEY
        template.setAppId(TargetTypeConstant.APP_ID);
        template.setAppkey(TargetTypeConstant.APP_KEY);
        //透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动

        template.setTransmissionContent(JSONObject.toJSONString(customParam1)); //透传内容  传入json,客户端自取
        template.set3rdNotifyInfo(get3rdNotifyInfo(msgPushStyle.getTitle(), msgPushStyle.getContent(), customParam1));
        template.setAPNInfo(getAPNPayload(msgPushStyle)); //ios消息推送
        return template;
    }
    /**
     * 第三方厂商通知
     *
     * @param title   标题
     * @param content 正文
     * @param payload 附带属性
     * @return
     */
    private static Notify get3rdNotifyInfo(String title, String content, Map<String, Object> payload) {
        Notify notify = new Notify();
        notify.setTitle(title);
        notify.setContent(content);
        notify.setPayload(JSONObject.toJSONString(payload));
        return notify;
    }
    //点击通知, 打开（自身）应用内任意页面
    public static StartActivityTemplate getStartActivityTemplate(MsgPushStyleDTO msgPushStyle) {
        StartActivityTemplate template = new StartActivityTemplate();
        // 设置APPID与APPKEY
        template.setAppId(TargetTypeConstant.APP_ID);
        template.setAppkey(TargetTypeConstant.APP_KEY);

        //设置展示样式
        template.setStyle(PushStyle.getStyle0(msgPushStyle)); //安卓消息推送
        template.setAPNInfo(getAPNPayload(msgPushStyle)); //ios消息推送

        //设置展示样式
        String intent = "intent:#Intent;launchFlags=0x04000000;action=android.intent.action.oppopush;package=com.htstar.ovms;component=com.htstar.ovms/io.dcloud.PandoraEntry;end";
        template.setIntent(intent);//最大长度限制为1000
        //template.setNotifyid(123); // 在消息推送的时候设置notifyid。如果需要覆盖此条消息，则下次使用相同的notifyid发一条新的消息。客户端sdk会根据notifyid进行覆盖。
        return template;
    }

    /**
     * 消息撤回模版
     *
     * @param taskId
     * @return
     */
    public static RevokeTemplate getRevokeTemplate(String taskId) {
        RevokeTemplate template = new RevokeTemplate();
        // 设置APPID与APPKEY

        template.setAppId(TargetTypeConstant.APP_ID);
        template.setAppkey(TargetTypeConstant.APP_KEY);
        template.setOldTaskId(taskId); //指定需要撤回消息对应的taskId
        template.setForce(false); // 客户端没有找到对应的taskid，是否把对应appid下所有的通知都撤回

        return template;
    }

    private static APNPayload getAPNPayload(MsgPushStyleDTO msgPushStyle) {
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge("0");
//        payload.setContentAvailable(1);
        //ios 12.0 以上可以使用 Dictionary 类型的 sound
//      payload.setSound("com.gexin.ios.silence");
        payload.setSound("default");
        payload.setContentAvailable(0);
        payload.setCategory("$button");//字典模式使用APNPayload.DictionaryAlertMsg
        Map<String, Object> customParam1 = new HashMap<>();
        customParam1.put("title", msgPushStyle.getTitle());
        customParam1.put("content", msgPushStyle.getContent());
        if(msgPushStyle.getMsgType() == 1){ //用车通知地址
            payload.addCustomMsg("payload", "/pages/usecar/usecar-detail?id="+msgPushStyle.getId()+"&title="+msgPushStyle.getTitle());
        }
        if(msgPushStyle.getMsgType() == 2){//公告通知地址
            payload.addCustomMsg("payload","/pages/publics/publics-detail?id="+msgPushStyle.getId()+"&title="+msgPushStyle.getTitle());
        }
        if(msgPushStyle.getMsgType() == 3){//消息提醒地址
            payload.addCustomMsg("payload","/pages/caution/caution-detail?id="+msgPushStyle.getId()+"&title="+msgPushStyle.getTitle());
        }
       // payload.addCustomMsg("payload",JSONObject.toJSONString(customParam1));
        payload.addCustomMsg("title", msgPushStyle.getTitle());
        payload.addCustomMsg("content", msgPushStyle.getContent());
        log.info("透传消息数据IOS{}",payload.getPayload());
        payload.setAlertMsg(getDictionaryAlertMsg(msgPushStyle));
        return payload;
    }

    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(MsgPushStyleDTO msgPushStyle) {
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setTitle(msgPushStyle.getTitle());
        alertMsg.setBody(msgPushStyle.getContent());
        return alertMsg;
    }

    /**
     * 需要使用iOS语音传输，请使用VoIPPayload代替APNPayload
     *
     * @return
     */
    private static VoIPPayload getVoIPPayload() {
        VoIPPayload payload = new VoIPPayload();
        JSONObject jo = new JSONObject();
        jo.put("key1", "value1");
        payload.setVoIPPayload(jo.toString());
        return payload;
    }
}
