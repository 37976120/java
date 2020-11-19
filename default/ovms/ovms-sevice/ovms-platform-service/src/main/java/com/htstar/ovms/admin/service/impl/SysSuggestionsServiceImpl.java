package com.htstar.ovms.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.entity.SysSuggestions;
import com.htstar.ovms.admin.api.req.PageSuggestionsReq;
import com.htstar.ovms.admin.api.vo.SysSuggestionsVO;
import com.htstar.ovms.admin.mapper.SysSuggestionsMapper;
import com.htstar.ovms.admin.service.SysSuggestionsService;
import com.htstar.ovms.common.core.util.R;
import org.springframework.stereotype.Service;

/**
 * 反馈建议
 *
 * @author flr
 * @date 2020-09-07 16:59:29
 */
@Service
public class SysSuggestionsServiceImpl extends ServiceImpl<SysSuggestionsMapper, SysSuggestions> implements SysSuggestionsService {

    @Override
    public R<Page<SysSuggestionsVO>> getPage(PageSuggestionsReq req) {
        Page<SysSuggestionsVO> re = this.baseMapper.getPage(req);
        return R.ok(re);
    }
}
