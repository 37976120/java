package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.htstar.ovms.enterprise.api.vo.CarOtherVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.xmlbeans.impl.jam.mutable.MElement;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 公车其他项目表

 *
 * @author lw
 * @date 2020-06-08 13:48:45
 */
@Data
@TableName("car_other_item")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "公车其他项目表 ")
public class CarOtherItem extends Model<CarOtherItem> {
private static final long serialVersionUID = 1L;

    /**
     * 其他项目主键
     */
    @TableId
    @ApiModelProperty(value="其他项目主键")
    private Integer id;
    /**
     * 项目种类
     */
    @ApiModelProperty(value="费用类型")
    @NotBlank(message = "费用类型")
    private Integer itemType;
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
     * 项目金额
     */
    @ApiModelProperty(value="项目金额",required = true)
    @NotBlank(message = "项目花费金额不能为空")
    private Integer itemMoney;
    /**
     * 项目办理时间
     */
    @ApiModelProperty(value="项目办理时间",required = true)
    @NotBlank(message = "费用产生时间不能为空")
    private LocalDateTime itemTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
    /**
     * 票据地址
     */
    @ApiModelProperty(value="票据地址")
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
    @ApiModelProperty(value = "用车记录")
    private Integer orderId;
    @ApiModelProperty(value ="司机的用户id")
    private Integer driverUserId;
    @TableField(exist = false)
    @ApiModelProperty(value = "其他费用批量登记")
    private List<CarOtherVo> carOtherVos;
    @TableField(exist = false)
    @ApiModelProperty(value="产生费用司机")
    private String driver;
    @ApiModelProperty(value = "审批退回备注")
    private String applyRemark;
}
