package com.htstar.ovms.enterprise.api.constant;

/**
 * Description: 流程操作类型 对应表 apply_process_record.operation_type
 * Author: flr
 * Date: Created in 2020/6/24
 * Company: 航通星空
 * Modified By:
 */
public interface ProcessOperationTypeConstant {
     //*操作类型：1=同意；2=拒绝；3=退回；4=修改；5=作废；

    /**
     * 撤回：撤回到第一步，只有申请人有权限，（配置的）
     *
     * 退回：退回到上一步，当前节点的操作人有权限，默认有
     *
     * 作废：当前节点的人（配置的），操作之后该申请只读
     */

    /**
     * 1=同意
     */
    int AGREE = 1;

    /**
     * 2=拒绝
     */
    int REFUSE = 2;

    /**
     * 3=退回 退回到上一步，当前节点的操作人有权限，默认有
     */
    int BACK = 3;

    /**
     * 4=修改
     */
    int UPDATE = 4;

    /**
     * 5=作废；
     */
    int ABAND = 5;

    /**
     * 6=撤回
     */
    int GO_START = 6;


    /**
     * 7=分配车辆
     */
    int DISTRIBUTION_CAR = 7;


    /**
     * 8=分配司机
     */
    int DISTRIBUTION_DRIVER = 8;

    /**
     * 9=提车上报
     */
    int GIVE_CAR_DATA = 9;

    /**
     * 10=还车上报
     */
    int RETURN_CAR_DATA = 10;

}
