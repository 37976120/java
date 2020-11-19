package com.htstar.ovms.msg.api.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class MsgPushLogVO extends Model<MsgPushLogVO> {
private static final long serialVersionUID = 1L;


    /**
     * 通知标题
     */
    @ApiModelProperty(value="通知标题")
    private String title;
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



}
