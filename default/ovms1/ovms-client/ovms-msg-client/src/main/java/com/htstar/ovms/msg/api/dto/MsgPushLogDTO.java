package com.htstar.ovms.msg.api.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;

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
public class MsgPushLogDTO extends Page {


    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value="消用户id；")
    private Integer userId;

    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value="消息类型：1=用车通知；2=公告通知；3=提醒消息；")
    private Integer msgType;




}
