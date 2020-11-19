package com.htstar.ovms.device.api.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HanGuJi
 * @Description: 监控列表
 * @date 2020/7/416:13
 */
@Data
@ApiModel(value = "监控列表")
public class MonitoringCountVO implements Serializable {
    @ApiModelProperty(value = "企业名称")
    private Page<MonitoringVO> monitoringVOPage;

    @ApiModelProperty(value = "已行驶总数")
    private int countCarStatus;

    @ApiModelProperty(value = "未行驶总数")
    private int countCarSends;
}
