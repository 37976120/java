package com.htstar.ovms.enterprise.api.constant;/**
 * Description:
 * Author: lw
 * Date: Created in 2020/7/22
 * Company: 航通星空
 * Modified By:
 */

import io.swagger.annotations.ApiModel;

/**
 * @Description:
 * @Author: lw
 * @CreateDate: 2020/7/22 15:20  
 */
@ApiModel("卡流水相关")
public interface CardCostInfoConstant {

    /**
     * 油卡
     */
    int FUEL_CARD=0;

    /**
     * etc卡
     */
    int ETC_CARD=1;

    /**
     * 充值
     */
    int RECHARGE=0;

    /**
     * 修改
     */
    int MODIFY=1;


    /**
     * 消费
     */
    int CONSUME=2;

    /**
     * 借记卡
     */
    int DEBIT_CARD=0;
    /**
     * 信用卡
     */
    int CREDIT_CARD=1;
}
