package com.htstar.ovms.msg.api.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author JinZhu
 * @Description:
 * @date 2020/7/2815:23
 */
@Data
@ApiModel(value = "个推传输对象")
public class MsgPushStyleDTO  {
    private static final long serialVersionUID = 1L;


   
    @ApiModelProperty(value="通知标题")
    private String title;

    @ApiModelProperty(value="通知内容")
    private String content;

    @ApiModelProperty(value="企业id ,向企业推送需要进行获取")
    private Integer etpId;

    @ApiModelProperty(value="用户id ,向当个用户推送需要进行获取")
    private Integer userId;

    @ApiModelProperty(value="消息类型")
    private Integer msgType;

    @ApiModelProperty(value="消息id")
    private Integer id;

    @ApiModelProperty(value="用车id")
    private Integer appCarId;


}
