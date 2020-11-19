package com.htstar.ovms.msg.api.constant;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author JinZhu
 * @Description: 个推样式公共成员和方法名
 * @date 2020/7/2815:23
 * Company: 航通星空
 * Modified By:
 */
public class MsgPushStyleConstant {
    /**
     * 收到通知是否响铃：true响铃，false不响铃。默认响铃。
     */
    public static final boolean RING = true;
    /**
     * 收到通知是否振动：true振动，false不振动。默认振动。
     */
    public static final boolean VIBRATE= true;
    /**
     * 通知是否可清除： true可清除，false不可清除。默认可清除。
     */
    public static final boolean CLEARABLE = true;
    /**
     * 	自定义铃声，请填写文件名，不包含后缀名(需要在客户端开发时嵌入)，个推通道下发有效
     * 客户端SDK最低要求 2.14.0.0
     * 服务端SDK最低要求 4.1.1.3
     */
    public static final String RING_NAME = "";
    /**
     * 角标, 必须大于0, 个推通道下发有效
     * 此属性目前仅针对华为 EMUI 4.1 及以上设备有效
     * 角标数字数据会和之前角标数字进行叠加；
     * 举例：角标数字配置1，应用之前角标数为2，发送此角标消息后，应用角标数显示为3。
     * 客户端SDK最低要求 2.14.0.0
     * 服务端SDK最低要求 4.1.1.3
     */
    public static final Integer BADGE_ADD_NUM = 1;
}
