package com.htstar.ovms.enterprise.api.vo;

import com.htstar.ovms.enterprise.api.entity.ApplyCostProcessRecord;
import com.htstar.ovms.enterprise.api.entity.ApplyCostVerifyNode;
import com.sun.scenario.effect.impl.prism.PrTexture;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Date: Created in 2020/7/14
 * Company: 航通星空
 * Modified By:
 * @author lw
 */
@Data
@ApiModel("审核记录")
public class ApplyCostProcessRecordVo extends ApplyCostProcessRecord {
    @ApiModelProperty("车牌号")
    private String licCode;
    @ApiModelProperty("申请人")
    private String applyName;
    @ApiModelProperty("部门名字")
    private String deptName;
    @ApiModelProperty("手机号")
    private String phone;

}
