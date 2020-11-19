package com.htstar.ovms.device.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 车辆地图关联表（标签表
 *
 * @author flr
 * @date 2020-06-19 17:53:42
 */
@Data
@TableName("driver_map_area_car_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车辆地图关联表（标签表）")
public class DriverMapAreaCarInfo extends Model<DriverMapAreaCarInfo> {
private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId
    @ApiModelProperty(value="自增id")
    private Integer id;
    @ApiModelProperty(value = "省级区域编码")
    private String mapAreaCode1;

    @ApiModelProperty(value = "市级区域编码")
    private String mapAreaCode2;

    @ApiModelProperty(value = "区级区域编码")
    private String mapAreaCode3;

    @ApiModelProperty(value = "车辆Id")
    private Integer  mapCarInfoId;

    @ApiModelProperty(value = "区域名称")
    private  String mapAreaName;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

}
