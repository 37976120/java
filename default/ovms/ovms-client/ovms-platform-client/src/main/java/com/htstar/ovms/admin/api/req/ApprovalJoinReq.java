package com.htstar.ovms.admin.api.req;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description: 审批加入
 * Author: flr
 * Date: Created in 2020/6/29
 * Company: 航通星空
 * Modified By:
 */
@Data
public class ApprovalJoinReq implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="主键ID",required = true)
    @NotNull(message = "请正确传参，主键ID为毕传")
    private Integer id;

    /**
     * 审批状态：0=待审批；1=拒绝加入；2=同意加入；
     */
    @ApiModelProperty(value="审批状态：0=待审批；1=拒绝加入；2=同意加入；",required = true)
    private Integer applyStatus;

    /**
     * 备注（20字以内）
     */
    @ApiModelProperty(value="备注（20字以内）")
    private String remark;
}
