package com.htstar.ovms.enterprise.msg.template;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 用车消息模板
 * Author: flr
 * Date: Created in 2020/8/12
 * Company: 航通星空
 * Modified By:
 */
@Getter
@AllArgsConstructor
public enum UseCarTemlateEnum {

    WAITE_VERIFY("用车申请审批","{0}提交的用车申请需要您审批，请及时处理"),
    WAITE_GIVE_CAR("派车审批", "{0}提交的用车申请需要您派车，请及时处理"),
    WAIT_RECIVE("收车审批", "{0}提交的还车申请需要您审批，请及时处理"),
    WAIT_GETCAR("提车提醒", "您的用车申请已审批通过，需要您或司机提车，请及时处理"),
    RECIVE_BACK("退回提醒", "您的还车申请已被退回，请修改后再进行提交"),
    REFUSE("拒绝提醒", "您的用车申请已被拒绝，请修改后再进行提交");

    private String title;

    private String content;
}
