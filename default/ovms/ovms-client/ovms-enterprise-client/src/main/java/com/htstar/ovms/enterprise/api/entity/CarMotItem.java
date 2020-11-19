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
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 公车年检表
 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Data
@TableName("car_mot_item")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公车年检表")
public class CarMotItem extends Model<CarMotItem> {
private static final long serialVersionUID = 1L;

    /**
     * 年检信息编号
     */
    @TableId
    @ApiModelProperty(value="年检信息编号")
    private Integer id;
    /**
     * 所属企业
     */
    @ApiModelProperty(value="所属企业")
    private Integer etpId;
    /**
     * 车辆编号
     */
    @ApiModelProperty(value="车辆编号",required = true)
    @NotBlank(message = "车辆信息不能为空")
    private Integer carId;
    /**
     * 年检时间
     */
    @ApiModelProperty(value="年检时间",required = true)
    @NotBlank(message = "年检日期不能为空")
    private LocalDateTime motTime;
    /**
     * 下次年检时间
     */
    @ApiModelProperty(value="下次年检时间",required = true)
    @NotBlank(message = "下次年检日期不能为空")
    private LocalDateTime nextTime;
    /**
     * 年检地点
     */
    @ApiModelProperty(value="年检地点")
    private String address;
    /**
     * 年检费用
     */
    @ApiModelProperty(value="年检费用",required = true)
    @NotBlank(message = "年检费用不能为空")
    private Integer motMoney;
    /**
     * 年检负责人
     */
    @ApiModelProperty(value="年检负责人",required = true)
    @NotBlank(message = "送检人不能为空")
    private String motAdmin;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    /**
     * 年检单据保存地址
     */
    @ApiModelProperty(value="年检单据保存地址")
    private String billAddr;
    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;
    /**
     * 删除状态:0=正常，1=已删除
     */
    @ApiModelProperty(value="删除状态:0=正常，1=已删除")
    private Integer delFlag;
    @ApiModelProperty(value="用户Id")
    private Integer userId;
    @ApiModelProperty(value=" 项目状态 0:待提交 1:待审核 2:已存档")
    private Integer itemStatus;
    @ApiModelProperty(value = "审批退回备注")
    private String applyRemark;
}
