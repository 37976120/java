package com.htstar.ovms.admin.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Description: 企业信息
 * Author: flr
 * Date: Created in 2020/6/28
 * Company: 航通星空
 * Modified By:
 */
@Data
public class EtpPrentVO {

    /**
     * 企业ID
     */
    @ApiModelProperty(value="企业ID")
    private Integer id;
    /**
     * 企业名称
     */
    @ApiModelProperty(value="企业")
    private String etpName;

    /**
     * 企业编码
     */
    @ApiModelProperty(value="企业编码")
    private String etpNo;
    @ApiModelProperty(value="企业/省级、机构")
    private String etpNames;
    private List<EtpPrentVO> etpTreeList;
}
