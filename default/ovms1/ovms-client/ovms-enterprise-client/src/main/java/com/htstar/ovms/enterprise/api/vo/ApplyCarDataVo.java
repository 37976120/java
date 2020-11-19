package com.htstar.ovms.enterprise.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.servlet.ServletContextListener;
import java.io.Serializable;

/**
 * Description:
 * Date: Created in 2020/7/16
 * Company: 航通星空
 * Modified By:
 * @author liuwei
 */
@Data
@ApiModel("用车数据总览")
public class ApplyCarDataVo implements Serializable {
    @ApiModelProperty(value = "用车总次数")
    private Integer sumCount=0;
    @ApiModelProperty(value = "普通用车次数")
    private Integer comCount=0;
    @ApiModelProperty(value = "私车公用次数")
    private Integer privateCount=0;
    @ApiModelProperty(value = "司机出车次数")
    private Integer driverCount=0;
    @ApiModelProperty(value = "自驾用车次数")
    private Integer selfDiverCount=0;
    @ApiModelProperty(value = "车辆空置数")
    private Integer carVacantCount=0;
    @ApiModelProperty(value = "车辆空置率")
    private String vacantRate="100%";
}
