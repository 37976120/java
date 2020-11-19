package com.htstar.ovms.enterprise.api.vo;

import com.htstar.ovms.enterprise.api.entity.CarFuelItem;
import com.htstar.ovms.enterprise.api.entity.CarInfo;
import lombok.Data;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/16
 * Company: 航通星空
 * Modified By:
 */
@Data
public class CarManageExprotVO {
    private CarInfo carInfo;
    private CarFuelItem carFuelItem;
}
