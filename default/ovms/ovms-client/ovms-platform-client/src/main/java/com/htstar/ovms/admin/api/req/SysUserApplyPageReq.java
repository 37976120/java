package com.htstar.ovms.admin.api.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/9/3
 * Company: 航通星空
 * Modified By:
 */
@Data
public class SysUserApplyPageReq  extends Page implements Serializable {
    private static final long serialVersionUID = 8654804333970992425L;

    @ApiModelProperty(value="企业Id", required = true)
    private Integer etpId;

}
