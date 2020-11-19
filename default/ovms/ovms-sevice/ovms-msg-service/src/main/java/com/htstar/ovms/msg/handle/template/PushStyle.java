package com.htstar.ovms.msg.handle.template;

import com.gexin.rp.sdk.template.style.AbstractNotifyStyle;
import com.gexin.rp.sdk.template.style.Style0;
import com.gexin.rp.sdk.template.style.Style6;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.msg.api.constant.MsgPushStyleConstant;
import com.htstar.ovms.msg.api.dto.MsgPushStyleDTO;
import com.htstar.ovms.msg.api.entity.MsgPushLog;
import com.htstar.ovms.msg.service.MsgPushLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * 推送样式
 * @author JinZhu
 * @Description:
 * @date 2020/7/2810:43
 */
public class PushStyle {



    /**
     * Style0 系统样式
     * @link http://docs.getui.com/getui/server/java/template/ 查看效果
     * @return
     */
    public static AbstractNotifyStyle getStyle0(MsgPushStyleDTO msgPushStyle) {
        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle(msgPushStyle.getTitle());
        style.setText(msgPushStyle.getContent());
//        // 配置通知栏图标
//         style.setLogo("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596195174721&di=922e82d0054f7b7054643ef0126f1c65&imgtype=0&src=http%3A%2F%2Fimg2.imgtn.bdimg.com%2Fit%2Fu%3D3557467182%2C2853025788%26fm%3D214%26gp%3D0.jpg"); //配置通知栏图标，需要在客户端开发时嵌入，默认为push.png
        // 配置通知栏网络图标
        // style.setLogoUrl("https://goss.veer.com/creative/vcg/veer/800water/veer-147482619.jpg");
//        // 配置自定义铃声(文件名，不需要后缀名)，需要在客户端开发时嵌入后缀名为.ogg的铃声文件
//        //style.setRingName("sound");
//        // 角标, 必须大于0, 个推通道下发有效; 此属性目前仅针对华为 EMUI 4.1 及以上设备有效
//        style.setBadgeAddNum(MsgPushStyleConstant.BADGE_ADD_NUM);
//        // 设置通知是否响铃，震动，或者可清除
        style.setRing(MsgPushStyleConstant.RING);
        style.setVibrate(MsgPushStyleConstant.VIBRATE);
        style.setClearable(MsgPushStyleConstant.CLEARABLE);
        //style.setChannel(msgPushStyle.getChannel());
        //style.setChannelName(msgPushStyle.getChannelName());
        //style.setChannelLevel(msgPushStyle.getChannelLevel()); //设置通知渠道重要性

        return style;
    }

    /**
     * Style6 展开式通知样式
     * @link http://docs.getui.com/getui/server/java/template/ 查看效果
     * @return
     */
    public static AbstractNotifyStyle getStyle6() {
        Style6 style = new Style6();
        // 设置通知栏标题与内容
        style.setTitle("请输入通知栏标题");
        style.setText("请输入通知栏内容");
        // 配置通知栏图标
        style.setLogo("icon.png"); //配置通知栏图标，需要在客户端开发时嵌入
        // 配置通知栏网络图标
        style.setLogoUrl("");
        // 三种方式选一种
        style.setBigStyle1("bigImageUrl"); //设置大图+文本样式
//        style.setBigStyle2("bigText"); //设置长文本+文本样式

        // 配置自定义铃声(文件名，不需要后缀名)，需要在客户端开发时嵌入后缀名为.ogg的铃声文件
        style.setRingName("sound");
        // 角标, 必须大于0, 个推通道下发有效; 此属性目前仅针对华为 EMUI 4.1 及以上设备有效
        style.setBadgeAddNum(1);
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        style.setChannel("通知渠道id");
        style.setChannelName("通知渠道名称");
        style.setChannelLevel(3); //设置通知渠道重要性
        return style;
    }
}
