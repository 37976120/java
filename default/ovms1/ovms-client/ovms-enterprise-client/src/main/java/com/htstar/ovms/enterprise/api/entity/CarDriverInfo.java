package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 司机
 *
 * @author lw
 * @date 2020-06-23 13:54:59
 */
@Data
@TableName("car_driver_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "司机")
public class CarDriverInfo extends Model<CarDriverInfo> {
private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty(value="主键")
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id",required = true)
    @NotBlank(message = "用户信息不能为空")
    private Integer userId;
    /**
     * 拿证日期
     */
    @ApiModelProperty(value="拿证日期")
    private LocalDateTime getLicenseTime;
    /**
     * 驾照类型 0:A1 1:A2 2:A3 3:B1 4:B2 5:c1
     */
    @ApiModelProperty(value="驾照类型 0:A1 1:A2 2:A3 3:B1 4:B2 5:c1")
    private Integer licenseType;
    /**
     * 司机状态 0:正常 1:请假
     */
    @ApiModelProperty(value="司机状态 0:正常 1:请假")
    private Integer driverStatus;
    /**
     * 删除状态 0:正常 1:删除
     */
    @ApiModelProperty(value="删除状态 0:正常 1:删除")
    private Integer delFlag;
    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建日期")
    private LocalDateTime createTime;
    /**
     * 修改日期
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value="修改日期")
    private LocalDateTime updateTime;
    @ApiModelProperty(value="企业ID")
    private Integer etpId;

}
