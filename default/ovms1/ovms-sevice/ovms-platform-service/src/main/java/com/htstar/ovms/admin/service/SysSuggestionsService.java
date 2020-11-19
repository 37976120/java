package com.htstar.ovms.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htstar.ovms.admin.api.entity.SysSuggestions;
import com.htstar.ovms.admin.api.req.PageSuggestionsReq;
import com.htstar.ovms.admin.api.vo.SysSuggestionsVO;
import com.htstar.ovms.common.core.util.R;

/**
 * 反馈建议
 *
 * @author flr
 * @date 2020-09-07 16:59:29
 */
public interface SysSuggestionsService extends IService<SysSuggestions> {

    R<Page<SysSuggestionsVO>> getPage(PageSuggestionsReq req);
}
