package com.htstar.ovms.admin.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.entity.SysSuggestions;
import com.htstar.ovms.admin.api.req.PageSuggestionsReq;
import com.htstar.ovms.admin.api.vo.SysSuggestionsVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 反馈建议
 *
 * @author flr
 * @date 2020-09-07 16:59:29
 */
@Mapper
public interface SysSuggestionsMapper extends BaseMapper<SysSuggestions> {

    @SqlParser(filter = true)
    Page<SysSuggestionsVO> getPage(PageSuggestionsReq req);
}
