package com.htstar.ovms.enterprise.api.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 订单所处流程状态
 * Author: flr
 * Date: 2020/7/17 10:09
 * Company: 航通星空
 * Modified By:
 */
@Getter
@AllArgsConstructor
public enum ProcessStatusEnum {
    WAIT_VERIFY(ProcessStatusConstant.WAIT_VERIFY, "等待审批"),
    VERIFY_REFUSE(ProcessStatusConstant.VERIFY_REFUSE, "审批拒绝"),


    WAIT_DISTRIBUTION(ProcessStatusConstant.WAIT_DISTRIBUTION,"等待分配"),
    WAIT_GIVECAR(ProcessStatusConstant.WAIT_GIVECAR, "等待交车"),
    GIVECAR_REFUSE(ProcessStatusConstant.GIVECAR_REFUSE, "交车拒绝"),


    WAIT_GETCAR(ProcessStatusConstant.WAIT_GETCAR, "等待提车"),
    GETCAR_REFUSE(ProcessStatusConstant.GETCAR_REFUSE, "提车拒绝"),


    WAIT_RETURNCAR(ProcessStatusConstant.WAIT_RETURNCAR, "等待还车"),

    WAIT_STA(ProcessStatusConstant.WAIT_STA, "等待开始用车"),


    WAIT_END(ProcessStatusConstant.WAIT_END, "等待结束用车"),


    WAIT_RECIVE(ProcessStatusConstant.WAIT_RECIVE, "等待收车"),

    RECIVE_BACK(ProcessStatusConstant.RECIVE_BACK, "收车退回"),

    GO_START(ProcessStatusConstant.GO_START,"已撤回"),

    FINISH(ProcessStatusConstant.FINISH, "完成");
    private int code;
    /**
     * 描述
     */
    private String description;
}
