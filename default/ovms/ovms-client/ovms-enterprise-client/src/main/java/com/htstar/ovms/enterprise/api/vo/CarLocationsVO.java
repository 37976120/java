package com.htstar.ovms.enterprise.api.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author HanGuJi
 * @Description: 车辆定位展示
 * @date 2020/6/2214:15
 */
@Data
@ApiModel(value = "车辆定位展示VO")
public class CarLocationsVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "车辆定位")
    private Page<CarLocationVO> carLocationVOS;

    @ApiModelProperty(value = "车辆定位经纬度集合")
    private List<CarLocationsLatLngVO> carLocationsLatLngVOS;

    @ApiModelProperty(value = "已行驶总数")
    private int countCarStatus;

    @ApiModelProperty(value = "未行驶总数")
    private int countCarSends;



}
