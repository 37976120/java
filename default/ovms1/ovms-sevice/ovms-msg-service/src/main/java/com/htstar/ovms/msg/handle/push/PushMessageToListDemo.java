package com.htstar.ovms.msg.handle.push;



import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;

import com.gexin.rp.sdk.base.impl.Target;

import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.msg.api.constant.TargetTypeConstant;
import com.htstar.ovms.msg.api.dto.CidUserNameDTO;
import com.htstar.ovms.msg.api.dto.MsgPushStyleDTO;

import com.htstar.ovms.msg.handle.template.PushTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.htstar.ovms.msg.handle.appinfo.AppInfo.push;

/**
 * 对指定用户列表推送消息相关demo
 *
 * @author zhangwf
 * @see
 * @since 2019-07-09
 */
@Slf4j
public class PushMessageToListDemo {

//    public static void main(String[] args) {
//        //String taskId = getContentId();
//       // pushToList(taskId);
////        String taskId = getContentIdOfGroupName("任务别名_toList");
////        stopTask(taskId);
//    }

    /**
     * 用于在推送时去查找对应的message
     *
     * @return
     */
    private static String getContentId(TransmissionTemplate template) {
        return push.getContentId(getListMessage(template));
    }

    /**
     * 对指定向不同用户列表推送不同消息
     * <p>
     * 场景1，对于抽奖活动的应用，需要对已知的某些用户推送中奖消息，就可以通过clientid列表方式推送消息。
     * 场景2，向新客用户发放抵用券，提升新客的转化率，就可以事先提取新客列表，将消息指定发送给这部分指定CID用户。
     *
     * @param
     */
    public static R pushToList(CidUserNameDTO cidUserNameDTOS, MsgPushStyleDTO msgPushStyleDTO) {
//         System.setProperty("gexin_pushList_needDetails", "true"); //配置返回每个用户返回用户状态，可选
//         System.setProperty("gexin_pushList_needAliasDetails", "true");  // 配置返回每个别名及其对应cid的用户状态，可选
        // 配置推送目标
        TransmissionTemplate template = PushTemplate.getTransmissionTemplate(msgPushStyleDTO);
        List targets = new ArrayList();
        Target target = new Target();
        target.setAppId(TargetTypeConstant.APP_ID);
        target.setClientId(cidUserNameDTOS.getCid());
        target.setAlias(cidUserNameDTOS.getUserName());
        targets.add(target);
        String taskId = getContentId(template);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToList(taskId, targets);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ret != null) {
            log.info(ret.getResponse().toString());
            if (ret.getResponse().get("result") == "NoValidPush") {
                return R.failed("NoValidPush");
            }
        } else {
            log.info("服务器响应异常");
        }
        return R.ok(ret.getResponse().get("result"));
    }

    /**
     * 向不同用户推送同一消息
     *
     * @param cidUserNameDTOS
     * @param msgPushStyleDTO
     * @return
     */
    public static R pushToUserList(CidUserNameDTO cidUserNameDTOS, MsgPushStyleDTO msgPushStyleDTO) {
//         System.setProperty("gexin_pushList_needDetails", "true"); //配置返回每个用户返回用户状态，可选
//         System.setProperty("gexin_pushList_needAliasDetails", "true");  // 配置返回每个别名及其对应cid的用户状态，可选
        // 配置推送目标
        TransmissionTemplate template = PushTemplate.getTransmissionTemplate(msgPushStyleDTO);
        List targets = new ArrayList();
        Target target = new Target();
        target.setAppId(TargetTypeConstant.APP_ID);
        target.setClientId(cidUserNameDTOS.getCid());
        target.setAlias(cidUserNameDTOS.getUserName());
        targets.add(target);
        String taskId = getContentId(template);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToList(taskId, targets);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ret != null) {
            log.info(ret.getResponse().toString());
            if (ret.getResponse().get("result") == "NoValidPush") {
                return R.failed("NoValidPush");
            }
        } else {
            log.info("服务器响应异常");
        }
        return R.ok(ret.getResponse().toString());
    }

    private static ListMessage getListMessage(TransmissionTemplate template) {
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(5 * 1000 * 3600);
        message.setPushNetWorkType(0);//判断客户端是否wifi环境下推送。1为仅在wifi环境下推送，0为不限制网络环境，默认不限
        // 厂商下发策略；1: 个推通道优先，在线经个推通道下发，离线经厂商下发(默认);2: 在离线只经厂商下发;3: 在离线只经个推通道下发;4: 优先经厂商下发，失败后经个推通道下发;
        message.setStrategyJson("{\"default\":1,\"ios\":2}");
        return message;
    }

    /**
     * 任务组名推送, 一个应用同时下发了n个推送任务，为了更好地跟踪这n个任务的推送效果，可以把他们组成一个任务组，在查询数据时，只需要输入该任务组名即可同时查到n个任务的数据结果。
     *
     * @param groupName
     * @return
     */
    private static String getContentIdOfGroupName(TransmissionTemplate template, String groupName) {
        return push.getContentId(getListMessage(template), groupName);
    }

    /**
     * 对正处于推送状态，或者未接收的消息停止下发
     *
     * @param taskId
     */
    private static void stopTask(String taskId) {
        boolean ret = push.stop(taskId);
    }
}
