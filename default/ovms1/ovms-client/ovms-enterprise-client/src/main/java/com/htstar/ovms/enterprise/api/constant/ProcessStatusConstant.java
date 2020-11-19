package com.htstar.ovms.enterprise.api.constant;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/8/3
 * Company: 航通星空
 * Modified By:
 */
public interface ProcessStatusConstant {

    /**
     * 等待审批
     */
    int WAIT_VERIFY = 1;

    /**
     * 审批拒绝
     */
    int VERIFY_REFUSE = 2;


    /**
     * 等待分配
     */
    int WAIT_DISTRIBUTION = 3;

    /**
     * 等待交车
     */
    int WAIT_GIVECAR = 4;

    /**
     * 交车拒绝
     */
    int GIVECAR_REFUSE = 5;


    /**
     * 等待提车
     */
    int WAIT_GETCAR = 6;

    /**
     * 提车拒绝
     */
    int GETCAR_REFUSE = 7;

    /**
     * 等待还车
     */
    int WAIT_RETURNCAR = 8;

    /**
     * 等待开始用车
     */
    int WAIT_STA = 9;

    /**
     * 等待结束用车
     */
    int WAIT_END = 10;

    /**
     * 等待收车
     */
    int WAIT_RECIVE = 11;

    /**
     * 收车退回
     */
    int RECIVE_BACK = 12;

    /**
     * 已撤回
     */
    int GO_START = 13;

    /**
     * 完成
     */
    int FINISH = 14;
}
