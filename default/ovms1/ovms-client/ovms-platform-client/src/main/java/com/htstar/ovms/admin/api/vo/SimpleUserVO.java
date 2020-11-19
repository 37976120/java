package com.htstar.ovms.admin.api.vo;

import com.htstar.ovms.admin.api.entity.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class SimpleUserVO {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键")
    private Integer userId;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value="姓名", required = true)
    private String nickName;

    /**
     * 手机号
     */
//    @Sensitive(type = SensitiveTypeEnum.MOBILE_PHONE)
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;
    /**
     * 部门ID
     */
    @ApiModelProperty(value = "所属部门")
    private Integer deptId;
    /**
     * 租户ID
     */
    @ApiModelProperty(value = "所属租户")
    private Integer etpId;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "所属部门名称")
    private String deptName;
    /**
     * 角色列表
     */
    @ApiModelProperty(value = "拥有的角色列表")
    private List<SysRole> roleList;

    @ApiModelProperty(value = "是否已经选中为审批人员：0=否；1=是")
    private Integer selectStatus = 0;

    @ApiModelProperty(value = "性别 0:女 1:男")
    private Integer sex;
}
