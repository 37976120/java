package com.htstar.ovms.enterprise.api.vo;

import com.htstar.ovms.enterprise.api.entity.CarDriverScheduleSetting;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/116:51
 */
@Data
public class CarDriverScheduleSettingVO extends CarDriverScheduleSetting {

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "驾照类型")
    private Integer licenseType;

    @ApiModelProperty(value = "公司名称")
    private String etpName;

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "子品牌名称")
    private String carSubBrand;
}
