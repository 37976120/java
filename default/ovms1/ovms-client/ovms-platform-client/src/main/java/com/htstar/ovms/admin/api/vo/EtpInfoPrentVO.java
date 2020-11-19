package com.htstar.ovms.admin.api.vo;

import com.htstar.ovms.admin.api.dto.DeptTree;
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
public class EtpInfoPrentVO {

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

    private List<EtpInfoPrentVO> etpTreeList;

    /**
     *父级ID
     */
    @ApiModelProperty(value = "父级ID")
    private String parentId;
    /**
     * 所属企业树
     */
    private List<EtpInfoPrentVO> children;
    /**
     * 所属部门树
     */
    private List<DeptTree> deptTreeList;
    /**
     * 所有所属企业的ID
     */
    private List<Integer> ids;
}
