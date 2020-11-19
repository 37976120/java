package com.htstar.ovms.device.api.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/23
 * Company: 航通星空
 * Modified By:
 */
@Data
public class FenceVO {
    /**
     * 围栏ID
     */
    @ApiModelProperty(value="围栏ID")
    private Integer fenceId;

    /**
     * 汽车ID
     */
    @ApiModelProperty(value="汽车ID")
    private Integer carId;

    /**
     * 设备序列号
     */
    @ApiModelProperty(value="设备序列号")
    private String deviceSn;

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
     * 单位：M
     */
    @ApiModelProperty(value="单位：M")
    private Integer radius;
    /**
     * 围栏状态：1=启用；0=禁用；
     */
    @ApiModelProperty(value="围栏状态：1=启用；0=禁用；")
    private Integer fenceStatus;

    /**
     * 提醒类型：0=不提醒；2=驶入提醒；3=驶出提醒；4=驶入驶出都提醒；
     */
    @ApiModelProperty(value="提醒类型：0=不提醒；2=驶入提醒；3=驶出提醒；4=驶入驶出都提醒；")
    private Integer remindType;

    @ApiModelProperty(value = "0 圆形 1 正方形")
    private Integer fenceType;


    @ApiModelProperty(value = "多边形 横坐标(纬度) 用逗号分隔")
    private String abscissas;

    @ApiModelProperty(value = "多边形 纵坐标(经度) 用逗号分隔")
    private String ordinates;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

}
