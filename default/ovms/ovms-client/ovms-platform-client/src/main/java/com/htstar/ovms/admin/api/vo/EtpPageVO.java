package com.htstar.ovms.admin.api.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/6/12
 * Company: 航通星空
 * Modified By:
 */
@Data
public class EtpPageVO {

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
     * 企业编码
     */
    @ApiModelProperty(value="企业编码")
    private String etpNo;

    /**
     * 管理员姓名
     */
    @ApiModelProperty(value="管理员姓名")
    private String adminName;
    /**
     * 联系方式
     */
    @ApiModelProperty(value="联系方式")
    private String contact;
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

    @ApiModelProperty(value="是否分配管理员：1=已分配，2=未分配；")
    private Integer distributeStatus;

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
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;
    /**
     * 删除状态:0=正常，1=已删除
     */
    @ApiModelProperty(value="删除状态:0=正常，1=已删除")
    private Integer delFlag;

    /**
     * 0正常 9-冻结
     */
    @ApiModelProperty(value = "冻结标记,9:冻结,0:正常")
    private String etpStatus;

    /**
     * 管理员手机号
     */
    @ApiModelProperty(value = "管理员手机号")
    private String adminPhone;
}
