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
public enum ProcessOrderDriveTypeEnum {
    DRIVER(1, "司机"),
    SELF(2, "自驾");

    private int code;
    /**
     * 描述
     */
    private String description;
}
