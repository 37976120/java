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
 * 企业广告
 *
 * @author lw
 * @date 2020-08-10 17:29:10
 */
@Data
@TableName("etp_advert")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "企业广告")
public class EtpAdvert extends Model<EtpAdvert> {
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
     * 排序
     */
    @ApiModelProperty(value="排序")
    private Integer sort;
    /**
     * 图片地址
     */
    @ApiModelProperty(value="图片地址")
    private String pictureAddr;
    /**
     * 广告标题
     */
    @ApiModelProperty(value="广告标题")
    private String advertTitle;
    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;
    /**
     * 点击次数
     */
    @ApiModelProperty(value="点击次数")
    private int clickCount;
    /**
     * 公告id
     */
    @ApiModelProperty(value="公告id")
    private Integer noticeId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 删除状态 0:正常 1:删除
     */
    @ApiModelProperty(value="删除状态 0:正常 1:删除")
    private Integer delFlag;
    @ApiModelProperty(value="发布人id")
    private Integer putUserId;

    @TableField(exist = false)
    @ApiModelProperty(value="企业名称")
    private String etpName;

    @TableField(exist = false)
    @ApiModelProperty(value="公告名字")
    private String noticeTitle;

}
