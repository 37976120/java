package com.htstar.ovms.report.api.vo;

import lombok.Data;

/**
 * Description: 车辆使用vo
 * Author: flr
 * Date: Created in 2020/7/30
 * Company: 航通星空
 * Modified By:
 */
@Data
public class CarUseVO {
    /**
     * 所有的车辆
     */
    private int allCarCount;

    /**
     * 使用的车辆ID
     */
    private int useCarCount;
}
