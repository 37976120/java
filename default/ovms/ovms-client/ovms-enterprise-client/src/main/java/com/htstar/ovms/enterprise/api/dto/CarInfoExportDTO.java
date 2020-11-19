package com.htstar.ovms.enterprise.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HanGuJi
 * @Description: 后台车辆信息查询条件
 * @date 2020/6/1716:28
 */
@Data
@ApiModel(value = "车辆信息查询条件")
public class CarInfoExportDTO {

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "所属企业")
    private Integer etpId ;

    @ApiModelProperty(value = "车辆类型")
    private Integer carType;

    @ApiModelProperty(value = "车辆绑定状态  1 已绑定，0 未绑定")
    private Integer bindStatus;
    @ApiModelProperty(value = "所属部门")
    private Integer deptId;
    @ApiModelProperty(value = "选择ids")
    private String ids;

    @ApiModelProperty(value = "属于用户id")
    private Integer userId;
}
