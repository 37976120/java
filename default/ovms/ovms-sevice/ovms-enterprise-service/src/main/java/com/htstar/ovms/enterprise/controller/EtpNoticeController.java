package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.EtpNotice;
import com.htstar.ovms.enterprise.api.req.NoticeReq;
import com.htstar.ovms.enterprise.service.EtpNoticeService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;



/**
 * 企业公告
 *
 * @author lw
 * @date 2020-08-10 11:38:31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/etpnotice" )
@Api(value = "etpnotice", tags = "企业公告管理")
public class EtpNoticeController {

    private final  EtpNoticeService etpNoticeService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param etpNotice 企业公告
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/page" )
    public R<IPage<EtpNotice>> getEtpNoticePage(@RequestBody  NoticeReq req) {
        return etpNoticeService.queryPage(req);
    }


    /**
     * 通过id查询企业公告
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(etpNoticeService.getById(id));
    }

    /**
     * 新增企业公告
     * @param etpNotice 企业公告
     * @return R
     */
    @ApiOperation(value = "新增企业公告", notes = "新增企业公告")
    @SysLog("新增企业公告" )
    @PostMapping
    public R save(@RequestBody EtpNotice etpNotice) {
        return etpNoticeService.saveNotice(etpNotice);
    }


    /**
     * 通过id删除企业公告
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除企业公告", notes = "通过id删除企业公告")
    @SysLog("通过id删除企业公告" )
    @DeleteMapping("/remove/{id}" )
    public R removeById(@PathVariable("id") Integer id) {
        return etpNoticeService.delNotice(id);
    }

}
