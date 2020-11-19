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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车辆围栏关系
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
@Data
@TableName("car_fence_relation")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车辆围栏关系")
public class CarFenceRelation extends Model<CarFenceRelation> {
private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId
    @ApiModelProperty(value="自增ID")
    private Integer id;
    /**
     * 围栏ID
     */
    @ApiModelProperty(value="围栏ID")
    private Integer fenceId;
    /**
     * 车辆ID
     */
    @ApiModelProperty(value="车辆ID")
    private Integer carId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;


}
