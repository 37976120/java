package com.htstar.ovms.report.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/30
 * Company: 航通星空
 * Modified By:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalVO {
    /**
     *    月份
     */
    private int md;
    /**
     *    行驶公里
     */
    private int mileaGeTal;
    /**
     *    时间（h）
     */
    private int timeLong;
    /**
     *   用油量（L）
     */
    private int consumPtionTal;
    /**
     *   百公里耗油（L）
     */
    private int hdredKm;
}
