package com.htstar.ovms.admin.api.req;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/10
 * Company: 航通星空
 * Modified By:
 */
@Data
public class PageSimpleUserReq extends Page implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色id集合")
    private List<Integer> role;
    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Integer deptId;


    @ApiModelProperty(value = "主键id")
    private Integer userId;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;


    @ApiModelProperty(value = "已选中的人员ID，逗号隔开")
    private String verifyUserList;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "性别 0:女 1:男")
    private Integer sex;


    /**
     * 服务端自动装填 企业ID
     */
    @ApiModelProperty(hidden = true)
    private Integer etpId;

    @ApiModelProperty(hidden = true)
    private String applyVerifyUserStr;


    @ApiModelProperty(value="申请类型:0=公车申请；1=私车公用；2=直接派车；")
    private Integer applyType;

    private String roleCode;
}
