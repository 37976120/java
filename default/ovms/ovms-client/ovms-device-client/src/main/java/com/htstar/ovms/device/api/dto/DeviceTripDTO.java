package com.htstar.ovms.device.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/6/2311:45
 */
@Data
public class DeviceTripDTO extends Page {

    @ApiModelProperty(value = "设备编号")
    private String deviceSn;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "车牌号")
    private String licCode;

    @ApiModelProperty(value = "企业id")
    private Integer etpId;
}
