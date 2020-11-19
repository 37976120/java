package com.htstar.ovms.report.api.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;


/**
 * 司机费用

 *
 * @author lw
 * @date 2020-08-01 10:08:26
 */
@Data
@TableName("report_driver_expense")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "司机费用 ")
public class ReportDriverExpense extends Model<ReportDriverExpense> {
private static final long serialVersionUID = 1L;

    /**
     * 司机用户id
     */
    @TableId
    @ApiModelProperty(value="司机用户id")
    private Integer driverUserId;
    /**
     * 年月戳
     */
    @ApiModelProperty(value="年月戳")
    private LocalDateTime monthShort;
    /**
     * 企业ID
     */
    @ApiModelProperty(value="企业ID")
    private Integer etpId;
    /**
     * 加油费用
     */
    @ApiModelProperty(value="加油费用")
    private Integer fuelCost=0;
    /**
     * 通行费
     */
    @ApiModelProperty(value="通行费")
    private Integer etcCost=0;
    /**
     * 停车费
     */
    @ApiModelProperty(value="停车费")
    private Integer stopCost=0;
    /**
     * 洗车费
     */
    @ApiModelProperty(value="洗车费")
    private Integer washCost=0;
    /**
     * 罚单费
     */
    @ApiModelProperty(value="罚单费")
    private Integer ticketCost=0;
    /**
     * 汽车用品
     */
    @ApiModelProperty(value="汽车用品")
    private Integer suppliesCost=0;
    /**
     * 其他费用
     */
    @ApiModelProperty(value="其他费用")
    private Integer otherCost=0;

    @TableField(exist = false)
    @ApiModelProperty(value = "司机姓名")
    private String driverName;

}
