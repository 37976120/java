package com.htstar.ovms.msg.api.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息推送记录
 *
 * @author htxk
 * @date 2020-08-14 16:10:21
 */
@Data
@ApiModel(value = "消息推送记录总数")
public class MsgPushLogCountVO extends Model<MsgPushLogCountVO> {

    /**
     * 用车通知总数
     */
    @ApiModelProperty(value="1=用车通知")
    private Integer msgCount1;
    /**
     * 公告通知总数
     */
    @ApiModelProperty(value="2=公告通知")
    private Integer msgCount2;
    /**
     * 提醒消息总数
     */
    @ApiModelProperty(value="3=提醒消息")
    private Integer msgCount3;



}
