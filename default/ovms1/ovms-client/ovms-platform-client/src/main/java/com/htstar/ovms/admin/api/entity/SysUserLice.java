
package com.htstar.ovms.admin.api.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户驾驶证
 *
 * @author lw
 * @date 2020-07-08 10:32:23
 */
@Data
@TableName("sys_user_lice")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户驾驶证")
public class SysUserLice extends Model<SysUserLice> {
private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="用户id")
    private Integer userId;
    /**
     * 驾驶证地址
     */
    @ApiModelProperty(value="驾驶证地址")
    private String licenseAddr;


}
