package com.htstar.ovms.msg.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author JinZhu
 * @Description:
 * @date 2020/7/3019:31
 */
@Data
@ApiModel(value = "个推cid用户关联传输对象")
public class MsgPushUserCidDTO extends Page {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="cid唯一标识,APP一个用户一个cid")
     private Integer cid;
    @ApiModelProperty(value="用户id和cid绑定")
     private Integer userId;

}
