package com.htstar.ovms.enterprise.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.htstar.ovms.enterprise.api.vo.VerifyUserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 费用审批配置
 *
 * @author lw
 * @date 2020-07-14 10:59:15
 */
@Data
@TableName("apply_cost_verify_node")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "费用审批配置")
public class ApplyCostVerifyNode extends Model<ApplyCostVerifyNode> {
private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId
    @ApiModelProperty(value="自增主键")
    private Integer id;
    /**
     * 企业id
     */
    @ApiModelProperty(value="企业id")
    private Integer etpId;
    /**
     * 审批类型 0:不审核直接存档 1:审核后存档
     */
    @ApiModelProperty(value="审批类型 0:不审核直接存档 1:审核后存档")
    private Integer verifyType;
    /**
     * [审批人员ID列表(有序)
     */
    @ApiModelProperty(value="[审批人员ID列表(有序)")
    private String verifyUserList;
    /**
     * 创建人id
     */
    @ApiModelProperty(value="创建人id")
    private Integer createUserId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    @TableField(exist = false)
    @ApiModelProperty(value="审批人员信息")
    private List<VerifyUserVO> verifyUserVOList;
}
