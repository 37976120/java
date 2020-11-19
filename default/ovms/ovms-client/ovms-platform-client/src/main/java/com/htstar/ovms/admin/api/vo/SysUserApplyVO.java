package com.htstar.ovms.admin.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/9/3
 * Company: 航通星空
 * Modified By:
 */
@Data
public class SysUserApplyVO {

    @ApiModelProperty(value="主键ID")
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value="用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value="密码")
    private String password;
    /**
     * 盐
     */
    @ApiModelProperty(value="盐")
    private String salt;
    /**
     * 手机号
     */
    @ApiModelProperty(value="手机号")
    private String phone;
    /**
     * 姓名
     */
    @ApiModelProperty(value="姓名")
    private String nickName;
    /**
     * 部门ID
     */
    @ApiModelProperty(value="部门ID")
    private Integer deptId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value="部门名称")
    private String deptName;

    @ApiModelProperty(value = "性别 0:女 1:男")
    private Integer sex;
    /**
     * 所属租户
     */
    @ApiModelProperty(value="所属租户")
    private Integer etpId;
    /**
     * 审批状态：0=待审批；1=拒绝加入；2=同意加入；
     */
    @ApiModelProperty(value="审批状态：0=待审批；1=拒绝加入；2=同意加入；")
    private Integer applyStatus;
    /**
     * 备注（20字以内）
     */
    @ApiModelProperty(value="备注（20字以内）")
    private String remark;

    @ApiModelProperty(value="是否是司机：0=否（默认），1=是")
    private int driverStatus;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;
}
