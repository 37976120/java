package com.htstar.ovms.enterprise.api.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 卡流水
 *
 * @author lw
 * @date 2020-07-22 14:25:29
 */
@Data
@TableName("card_cost_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "卡流水")
public class CardCostInfo extends Model<CardCostInfo> {
private static final long serialVersionUID = 1L;

    /**
     * 费用流水主键
     */
    @TableId
    @ApiModelProperty(value="费用流水主键")
    private Integer id;
    /**
     * 企业id
     */
    @ApiModelProperty(value="企业id")
    private Integer etpId;
    /**
     * 卡id
     */
    @ApiModelProperty(value="卡id")
    private Integer cardId;
    /**
     * 卡类型0:油卡 1:etc卡
     */
    @ApiModelProperty(value="卡类型0:油卡 1:etc卡")
    private Integer cardType;
    /**
     * 车辆id
     */
    @ApiModelProperty(value="车辆id")
    private Integer carId;

    @ApiModelProperty(value="操作人")
    private Integer userId;
    /**
     * etc开始地址,油卡加油地址
     */
    @ApiModelProperty(value="etc开始地址,油卡加油地址")
    private String staAddr;
    /**
     * etc目的地
     */
    @ApiModelProperty(value="etc目的地")
    private String endAddr;
    /**
     * 油号 0:0号,1:89 2:92 3:95 4:98
     */
    @ApiModelProperty(value="油号 0:0号,1:89 2:92 3:95 4:98")
    private Integer fuelType;
    /**
     * 操作类型0:充值 1:修改 2:消费
     */
    @ApiModelProperty(value="操作类型0:充值 1:修改 2:消费")
    private Integer actionType;
    /**
     * 费用
     */
    @ApiModelProperty(value="费用")
    private Integer cost;
    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;
    /**
     * 添加时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="添加时间")
    private LocalDateTime createTime;
    /**
     * 是否删除 0:正常 1:删除
     */
    @ApiModelProperty(value="是否删除 0:正常 1:删除")
    private Integer delFlag;

    @ApiModelProperty(value="费用产生时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime costTime;
    @ApiModelProperty(value="操作后余额")
    private Integer balance;
    @ApiModelProperty(value = "车牌号")
    @TableField(exist = false)
    private String licCode;
    @TableField(exist = false)
    @ApiModelProperty(value = "操作人姓名")
    private String userName;
}
