package com.htstar.ovms.enterprise.api.constant;

/**
 * Description: 审批类型：1=待我审批；2=已审批；3=抄送我的；4=我发起的；3=抄送我的；
 * Author: flr
 * Date: Created in 2020/7/3
 * Company: 航通星空
 * Modified By:
 */
public interface ProcessTaskTypeConstant {

    /**
     * 1=等待审批
     */
    int WAIT_ME = 1;

    /**
     * 2=已审批；
     */
    int DONE = 2;

    /**
     * 3=抄送我的；
     */
    int CC_ME = 3;

    /**
     * 4=我发起的；
     */
    int I_START = 4;

}
