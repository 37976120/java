package com.htstar.ovms.device.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author HanGuJi
 * @Description: 监控列表
 * @date 2020/7/416:13
 */
@Data
@ApiModel(value = "监控列表不带分页查询全部经纬度")
public class MonitoringLatLngCountVO implements Serializable {

    @ApiModelProperty(value = "车辆定位经纬度集合")
    private List<MonitoringLatLngVO> monitoringLatLngVOS;

    @ApiModelProperty(value = "已行驶总数")
    private int countCarStatus;

    @ApiModelProperty(value = "未行驶总数")
    private int countCarSends;

}
