package com.htstar.ovms.enterprise.api.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 * Author: lw
 * Date: Created in 2020/8/10
 * Company: 航通星空
 * Modified By:
 */
@Data
@ApiModel("公告管理查询条件")
public class NoticeReq extends Page {
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("企业Id")
    private Integer etpId;
}
