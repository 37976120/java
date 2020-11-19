package com.htstar.ovms.enterprise.api.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/6/23
 * Company: 航通星空
 * 档案管理
 * Modified By:
 * @author liuwei
 */
@Data
@ApiModel(value = "档案管理模块查询条件")
public class CarFileManageReq extends Page implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="姓名或手机")
    private String name;
    @ApiModelProperty(value="卡名或卡号")
    private String card;
    @ApiModelProperty(hidden = true,value="企业ID")
    private Integer etpId;
    @ApiModelProperty("部门id ")
    private Integer deptId;
    @ApiModelProperty("删除编号")
    private String ids;

}
