package com.htstar.ovms.admin.api.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.htstar.ovms.admin.api.entity.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/9/7
 * Company: 航通星空
 * Modified By:
 */
@Data
public class SysSuggestionsVO {
    /**
     *
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 反馈建议内容
     */
    @ApiModelProperty(value="反馈建议内容")
    private String suggestionsContent;
    /**
     * 建议人ID
     */
    @ApiModelProperty(value="建议人ID")
    private Integer createUserId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人姓名")
    private String nickName;


    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 租户ID
     */
    @ApiModelProperty(value = "所属租户ID")
    private Integer etpId;

    /**
     * 租户ID
     */
    @ApiModelProperty(value = "所属租户")
    private String etpName;

    /**
     * 角色列表
     */
    @ApiModelProperty(value = "拥有的角色列表")
    private List<SysRole> roleList;
}
