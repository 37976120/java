package com.htstar.ovms.enterprise.api.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/11
 * Company: 航通星空
 * Modified By:
 */
@Data
public class CarManageVO implements Serializable {
    @ApiModelProperty(value="车辆ID")
    private Integer carId;
    @ApiModelProperty(value="车牌号")
    private String licCode;
    @ApiModelProperty(value="车型车系，暂时不传")
    private String  carModel;
    @ApiModelProperty(value="部门Id")
    private Integer deptId;
    @ApiModelProperty(value="累积费用")
    private Integer sumMoney;
    @ApiModelProperty(value="上次时间")
    private Date lastTime;
    @ApiModelProperty(value="下次时间")
    private Date nextTime;
    @ApiModelProperty(value="距下次天数")
    private Integer nextDays;
    @ApiModelProperty(value="项目状态 0有效 1过期")
    private Integer itemStatus;
    @ApiModelProperty(value="项目名称")
    private String itemName;
    @ApiModelProperty(value="部门名称")
    private String DeptName;

}
