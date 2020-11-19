package com.htstar.ovms.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.entity.SysSuggestions;
import com.htstar.ovms.admin.api.req.PageSuggestionsReq;
import com.htstar.ovms.admin.api.vo.SysSuggestionsVO;
import com.htstar.ovms.admin.service.SysSuggestionsService;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 反馈建议
 *
 * @author flr
 * @date 2020-09-07 16:59:29
 */
@RestController
@AllArgsConstructor
@RequestMapping("/suggestion" )
@Api(value = "suggestion", tags = "反馈建议管理")
public class SysSuggestionsController {

    private final  SysSuggestionsService sysSuggestionsService;

    /**
     * 分页查询
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/page" )
    public R<Page<SysSuggestionsVO>> getSysSuggestionsPage(@RequestBody PageSuggestionsReq req) {
        return sysSuggestionsService.getPage(req);
    }


    /**
     * 通过id查询反馈建议
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(sysSuggestionsService.getById(id));
    }

    /**
     * 新增反馈建议
     * @param sysSuggestions 反馈建议
     * @return R
     */
    @ApiOperation(value = "新增反馈建议", notes = "新增反馈建议")
    @SysLog("新增反馈建议" )
    @PostMapping
    public R save(@RequestBody SysSuggestions sysSuggestions) {
        if (sysSuggestions == null){
            return R.failed("请正确传参");
        }
        if (StrUtil.isBlank(sysSuggestions.getSuggestionsContent())){
            return R.failed("请填写反馈建议");
        }
        if (sysSuggestions.getCreateUserId() == null){
            sysSuggestions.setCreateUserId(SecurityUtils.getUser().getId());
        }
        return R.ok(sysSuggestionsService.save(sysSuggestions));
    }

    /**
     * 修改反馈建议
     * @param sysSuggestions 反馈建议
     * @return R
     */
    @ApiOperation(value = "修改反馈建议", notes = "修改反馈建议")
    @SysLog("修改反馈建议" )
    @PutMapping
    public R updateById(@RequestBody SysSuggestions sysSuggestions) {
        return R.ok(sysSuggestionsService.updateById(sysSuggestions));
    }

    /**
     * 通过id删除反馈建议
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除反馈建议", notes = "通过id删除反馈建议")
    @SysLog("通过id删除反馈建议" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(sysSuggestionsService.removeById(id));
    }

}
