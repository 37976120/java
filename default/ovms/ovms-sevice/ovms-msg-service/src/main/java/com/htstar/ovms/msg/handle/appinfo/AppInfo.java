package com.htstar.ovms.msg.handle.appinfo;

import com.gexin.rp.sdk.http.IGtPush;
import com.htstar.ovms.msg.api.constant.TargetTypeConstant;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/2811:29
 */
public class AppInfo {
    /**
     * 推送对象,负责推送
     */
    public static IGtPush push = new IGtPush( "http://sdk.open.api.igexin.com/apiex.htm",TargetTypeConstant.APP_KEY, TargetTypeConstant.MASTER_SECRET);
}
