package com.htstar.ovms.report.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javafx.scene.chart.ValueAxis;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 费用报表数据
 *
 * @author lw
 * @date 2020-07-27 15:19:34
 */
@Data
@TableName("report_expense")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "费用报表")
public class ReportExpense extends Model<ReportExpense> {
    private static final long serialVersionUID = 1L;

    /**
     * 费用年月
     */
    @ApiModelProperty(value = "费用年月")
    private LocalDateTime monthShort;
    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    private Integer etpId;
    /**
     * 车辆id
     */
    @ApiModelProperty(value = "车辆id")
    private Integer carId;

    /**
     * 加油费用
     */
    @ApiModelProperty(value = "加油费用")
    private Integer fuelCost = 0;
    /**
     * 通行费用
     */
    @ApiModelProperty(value = "通行费用")
    private Integer etcCost = 0;
    /**
     * 保险费用
     */
    @ApiModelProperty(value = "保险费用")
    private Integer insCost = 0;
    /**
     * 保养费用
     */
    @ApiModelProperty(value = "保养费用")
    private Integer maiCost = 0;
    /**
     * 年检费用
     */
    @ApiModelProperty(value = "年检费用")
    private Integer motCost = 0;
    /**
     * 维修费用
     */
    @ApiModelProperty(value = "维修费用")
    private Integer repairCost = 0;
    /**
     * 停车费
     */
    @ApiModelProperty(value = "停车费")
    private Integer stopCost = 0;
    /**
     * 洗车费
     */
    @ApiModelProperty(value = "洗车费")
    private Integer washCost = 0;
    /**
     * 罚单费
     */
    @ApiModelProperty(value = "罚单费")
    private Integer ticketCost = 0;
    /**
     * 汽车用品
     */
    @ApiModelProperty(value = "汽车用品")
    private Integer suppliesCost = 0;
    /**
     * 其他费用
     */
    @ApiModelProperty(value = "其他费用")
    private Integer otherCost = 0;
    @ApiModelProperty(value = "申请用车次数")
    private Integer applyCarCount = 0;
    @ApiModelProperty(value = "版本号")
    private Integer reportVersion = 0;

    @ApiModelProperty(value = "用车人")
    private Integer carUserId = 0;

    @TableField(exist = false)
    @ApiModelProperty(value = "车牌号")
    private String licCode;
    @TableField(exist = false)
    @ApiModelProperty(value = "月份")
    private String monthValue;

}
