package com.htstar.ovms.msg.api.entity;

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
 * 消息推送记录
 *
 * @author htxk
 * @date 2020-08-14 16:10:21
 */
@Data
@TableName("msg_push_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "消息推送记录")
public class MsgPushLog extends Model<MsgPushLog> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="自增id")
    private Integer id;
    /**
     * 通知标题
     */
    @ApiModelProperty(value="通知标题")
    private String title;
    /**
     * 用户ID
     */
    @ApiModelProperty(value="用户ID")
    private Integer userId;
    /**
     * 消息类型：1=用车通知；2=公告通知；3=提醒消息；
     */
    @ApiModelProperty(value="消息类型：1=用车通知；2=公告通知；3=提醒消息；")
    private Integer msgType;
    /**
     * 消息内容
     */
    @ApiModelProperty(value="消息内容")
    private String content;
    /**
     * 消息状态
     */
    @ApiModelProperty(value="1 已读 0 未读")
    private Integer msgStatus;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty(value="用车申请/费用审批id")
    private Integer appCarId;
    @ApiModelProperty(value="提醒消息类型：1，费用审批 2，成员加入 3警报提醒")
    private Integer remindType;


}
