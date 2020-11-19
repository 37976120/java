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
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 企业公告
 *
 * @author lw
 * @date 2020-08-10 11:38:31
 */
@Data
@TableName("etp_notice")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "企业公告")
public class EtpNotice extends Model<EtpNotice> {
private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty(value="主键")
    private Integer id;
    /**
     * 企业id
     */
    @ApiModelProperty(value="企业id")
    private Integer etpId;
    /**
     * 公告标题
     */
    @ApiModelProperty(value="公告标题")
    private String noticeTitle;
    /**
     * 公告内容
     */
    @ApiModelProperty(value="公告内容")
    private String noticeContent;
    /**
     * 发布人id
     */
    @ApiModelProperty(value="发布人id")
    private Integer putUserId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 删除标记 0:正常 1:删除
     */
    @ApiModelProperty(value="删除标记 0:正常 1:删除")
    private Integer delFlag;

    @TableField(exist = false)
    @ApiModelProperty(value="企业名称")
    private String etpName;

    @TableField(exist = false)
    @ApiModelProperty(value="发布人员")
    private String putUserName;

}
