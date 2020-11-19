package com.htstar.ovms.admin.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业表
 *
 * @author flr
 * @date 2020-08-04 09:56:08
 */
@Data
@TableName("etp_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "企业表")
public class EtpInfo extends Model<EtpInfo> {
private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    @TableId
    @ApiModelProperty(value="企业ID")
    private Integer id;
    /**
     * 企业名称
     */
    @ApiModelProperty(value="企业名称")
    private String etpName;
    /**
     * 企业编号
     */
    @ApiModelProperty(value="企业编号")
    private String etpNo;
    /**
     * 管理员姓名
     */
    @ApiModelProperty(value="管理员姓名")
    private String adminName;
    /**
     * 企业logo
     */
    @ApiModelProperty(value="企业logo")
    private String etpLogo;
    /**
     * 联系方式
     */
    @ApiModelProperty(value="联系方式")
    private String contact;
    /**
     * 所属行业（10字）
     */
    @ApiModelProperty(value="所属行业（10字）")
    private String industry;
    /**
     * 企业描述(100字)
     */
    @ApiModelProperty(value="企业描述(100字)")
    private String etpDesc;
    /**
     * 企业地址
     */
    @ApiModelProperty(value="企业地址")
    private String etpAddr;
    /**
     * 企业类型：1=试用企业，2=正式企业
     */
    @ApiModelProperty(value="企业类型：1=试用企业，2=正式企业")
    private Integer etpType;
    /**
     * 开始时间
     */
    @ApiModelProperty(value="开始时间")
    private LocalDate staTime;
    /**
     * 到期时间
     */
    @ApiModelProperty(value="到期时间")
    private LocalDate endTime;
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
    /**
     * 是否分配管理员：1=已分配，2=未分配；
     */
    @ApiModelProperty(value="是否分配管理员：1=已分配，2=未分配；")
    private Integer distributeStatus;
    /**
     * 冻结标记,9:冻结,0:正常
     */
    @ApiModelProperty(value="冻结标记,9:冻结,0:正常")
    private Integer etpStatus;
    /**
     * 删除状态:0=正常，1=已删除
     */
    @ApiModelProperty(value="删除状态:0=正常，1=已删除")
    private Integer delFlag;


}
