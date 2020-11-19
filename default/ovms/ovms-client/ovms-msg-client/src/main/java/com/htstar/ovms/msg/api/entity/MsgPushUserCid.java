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

import java.time.LocalDateTime;

/**
 * @author JinZhu
 * @Description:
 * @date 2020/7/3019:31
 */
@Data
@TableName("msg_push_user_cid")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "个推用户Cid绑定表 ")
public class MsgPushUserCid extends Model<MsgPushUserCid> {
    private static final long serialVersionUID = 1L;
    /**
     * 自增ID
     */
    @TableId
    @ApiModelProperty(value="自增ID")
     private Integer id;
    /**
     * cid唯一标识
     */
    @ApiModelProperty(value="cid唯一标识,APP一个用户一个cid")
     private String cid;
    /**
     * 用户id和cid绑定
     */
    @ApiModelProperty(value="用户id和cid绑定")
     private Integer userId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */

    @TableField( fill = FieldFill.UPDATE)
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;

}
