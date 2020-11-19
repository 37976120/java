package com.htstar.ovms.device.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author HanGuJi
 * @Description: 监控请求参数
 * @date 2020/7/415:46
 */
@Data
@ApiModel(value = "监控请求参数")
public class MonitoringDTO extends Page {
    @ApiModelProperty(value = "企业id")
    private List<Integer> etpIds;

    @ApiModelProperty(value = "车辆id")
    private List<Integer> carIds;

    @ApiModelProperty(value = "部门id")
    private List<Integer> deptIds;

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "车辆状态")
    private Integer online;

    @ApiModelProperty(value = "省级区域编码")
    private String mapAreaCode1;

    @ApiModelProperty(value = "市级区域编码")
    private String mapAreaCode2;

    @ApiModelProperty(value = "区级区域编码")
    private String mapAreaCode3;
}
