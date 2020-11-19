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
 * 围栏
 *
 * @author flr
 * @date 2020-06-23 16:36:06
 */
@Data
@TableName("fence")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "围栏")
public class Fence extends Model<Fence> {
private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId
    @ApiModelProperty(value="自增ID")
    private Integer id;
    /**
     * 围栏名称（长度20）
     */
    @ApiModelProperty(value="围栏名称（长度20）")
    private String fenceName;

    /**
     * 设置围栏时使用的地图 1：高德 2：谷歌
     */
    @ApiModelProperty(value="设置围栏时使用的地图 1：高德 2：谷歌")
    private Integer mapType;
    /**
     * 备注（长度50）
     */
    @ApiModelProperty(value="备注（长度50）")
    private String remark;
    /**
     * 纬度
     */
    @ApiModelProperty(value="纬度")
    private Double lat;
    /**
     * 经度
     */
    @ApiModelProperty(value="经度")
    private Double lng;
    /**
     * 半径 单位：M
     */
    @ApiModelProperty(value="半径 单位：M")
    private Integer radius;
    /**
     * 围栏状态：1=启用；0=禁用；
     */
    @ApiModelProperty(value="围栏状态：1=启用；0=禁用；")
    private Integer fenceStatus;
    /**
     * 创建者ID
     */
    @ApiModelProperty(value="创建者ID")
    private Integer createUser;

    @ApiModelProperty(value = "多边形 横坐标(纬度) 用逗号分隔")
    private String abscissas;

    @ApiModelProperty(value = "多边形 纵坐标(经度) 用逗号分隔")
    private String ordinates;

    @ApiModelProperty(value = "0 圆形 1 正方形")
    private Integer fenceType;

    @ApiModelProperty(value = "公司Id")
    private Integer etpId = null;


    @ApiModelProperty(value="提醒类型：0=不提醒；2=驶入提醒；3=驶出提醒；4=驶入驶出都提醒；")
    private Integer remindType;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;


}
