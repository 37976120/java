package com.htstar.ovms.msg.api.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/8/12
 * Company: 航通星空
 * Modified By:
 */
@Data
public class BaseAppPush {
    /**
     * 通知标题
     */
    @ApiModelProperty(value="通知标题")
    @NotBlank(message = "通知标题不能为空")
    private String title;
    /**
     * 通知内容；
     */
    @ApiModelProperty(value="通知内容")
    @NotBlank(message = "通知内容不能为空")
    private String content;

    /**
     * 消息类型；
     */
    @ApiModelProperty(value="消息类型 1用车管理 , 2公告管理 , 3 消息提醒")
    @NotNull(message = "通知内容不能为空")
    private Integer msgType;

    @ApiModelProperty(value="用车id")
    private Integer appCarId;

    @ApiModelProperty(value="提醒消息类型：1，费用审批 2，成员加入 3警报提醒")
    private Integer remindType;



}
