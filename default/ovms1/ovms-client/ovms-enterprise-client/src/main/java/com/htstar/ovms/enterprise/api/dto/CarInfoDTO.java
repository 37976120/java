package com.htstar.ovms.enterprise.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javafx.scene.chart.ValueAxis;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author HanGuJi
 * @Description: 后台车辆信息查询条件
 * @date 2020/6/1716:28
 */
@Data
@ApiModel(value = "车辆信息查询条件")
public class CarInfoDTO extends Page {

    @ApiModelProperty(value = "车牌号")
    private String licCode;
    @ApiModelProperty(value="企业ID集合")
    private List<Integer> etpIds;
    @ApiModelProperty(value = "所属企业")
    private Integer etpId ;
    @ApiModelProperty(value = "父级企业")
    private Integer parentId ;
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

    @ApiModelProperty(value = "购置日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime purchaseDate;

    @ApiModelProperty(value = "购置价格")
    private String purchaseValue;

    @ApiModelProperty(value = "注销日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime invalidDate;
    @ApiModelProperty(value = "排班时间")
    private Integer faleg = 1;
}
