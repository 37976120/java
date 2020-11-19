package com.htstar.ovms.device.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/7/710:20
 */
@Data
public class FenceDTO extends Page {
    @ApiModelProperty(value = "企业id")
    private Integer etpId = null;

    @ApiModelProperty(value = "围栏名称")
    private String fenceName;

    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "围栏id")
    private Integer fenceId;

    @ApiModelProperty(value = "车辆id")
    private Integer carId;

    @ApiModelProperty(value = "排班id")
    private Integer seetingId;

}
