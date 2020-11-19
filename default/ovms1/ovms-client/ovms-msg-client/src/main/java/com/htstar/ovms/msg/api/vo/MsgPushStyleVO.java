package com.htstar.ovms.msg.api.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author JinZhu
 * @Description:
 * @date 2020/7/2815:23
 */
@Data
@ApiModel(value = "Style样式视图对象")
public class MsgPushStyleVO implements Serializable {

    @ApiModelProperty(value="通知标题")
    private String title;
    @ApiModelProperty(value="通知内容")
    private String text;
    @ApiModelProperty(value="包含后缀名（需要在客户端开发时嵌入），如icon.png小LOGO，默认push_small.png，需要提前内置到客户端")
    private String logo;
    @ApiModelProperty(value="通知图标URL地址，小米、华为有些机型不支持此参数")
    private String logoUrl;
    @ApiModelProperty(value="通知渠道id，唯一标识")
    private String channel;
    @ApiModelProperty(value="通知渠道名称")
    private String channelName;
    @ApiModelProperty(value="该字段代表通知渠道重要性，具体值有0、1、2、3、4;设置之后不能修改")
    private Integer channelLevel;
    @ApiModelProperty(value="通知展示大图样式，参数是大图的URL地址")
    private String bigStyle1;
    @ApiModelProperty(value="通知展示文本+长文本样式，参数是长文本")
    private String bigStyle2;
}
