package com.htstar.ovms.enterprise.api.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 公车表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Data
public class CarInfoVo extends Model<CarInfoVo> {
    private static final long serialVersionUID = 1L;

    /**
     * 车辆Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "车辆Id")
    private Integer id;
    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String licCode;


    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private String deptName;

    /**
     * 座位数
     */
    @ApiModelProperty(value = "座位数")
    private Integer seateNum;
    /**
     * 级别
     */
    @ApiModelProperty(value = "级别")
    private String levelName;



    @ApiModelProperty(value = "车辆类型")
    private String carTypeName;
    @ApiModelProperty(value = "车辆是否可以分配")
    private Integer status;



}
