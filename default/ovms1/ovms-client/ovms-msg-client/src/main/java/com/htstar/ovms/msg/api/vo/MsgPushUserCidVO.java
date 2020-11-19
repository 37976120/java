package com.htstar.ovms.msg.api.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author JinZhu
 * @Description:
 * @date 2020/7/3019:31
 */
@Data
@ApiModel(value = "个推cid用户关联视图对象")
public class MsgPushUserCidVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="cid唯一标识,APP一个用户一个cid")
     private String cid;
    @ApiModelProperty(value="用户账号")
     private String userName;

}
