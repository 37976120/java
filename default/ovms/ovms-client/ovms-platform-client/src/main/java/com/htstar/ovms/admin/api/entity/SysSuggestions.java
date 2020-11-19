package com.htstar.ovms.admin.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 反馈建议
 *
 * @author flr
 * @date 2020-09-07 16:59:29
 */
@Data
@TableName("sys_suggestions")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "反馈建议")
public class SysSuggestions extends Model<SysSuggestions> {
private static final long serialVersionUID = 1L;

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
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;


}
