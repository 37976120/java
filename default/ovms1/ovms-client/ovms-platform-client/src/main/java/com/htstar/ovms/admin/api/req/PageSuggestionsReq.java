package com.htstar.ovms.admin.api.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/9/7
 * Company: 航通星空
 * Modified By:
 */
@Data
public class PageSuggestionsReq extends Page implements Serializable {
    private static final long serialVersionUID = 1L;
}
