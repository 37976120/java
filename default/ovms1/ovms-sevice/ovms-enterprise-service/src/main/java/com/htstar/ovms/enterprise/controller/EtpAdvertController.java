package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.EtpAdvert;
import com.htstar.ovms.enterprise.api.req.NoticeReq;
import com.htstar.ovms.enterprise.service.EtpAdvertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 企业广告
 *
 * @author lw
 * @date 2020-08-10 17:29:10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/etpadvert" )
@Api(value = "etpadvert", tags = "企业广告管理")
public class EtpAdvertController {

    private final  EtpAdvertService etpAdvertService;

    /**
     * 分页查询
     * @param req 分页对象
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @PostMapping("/page" )
    public R<IPage<EtpAdvert>> getEtpAdvertPage(@RequestBody NoticeReq req) {
        return R.ok(etpAdvertService.queryPage(req));
    }


    /**
     * 通过id查询企业广告
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R<EtpAdvert> getById(@PathVariable("id" ) Integer id) {
        return R.ok(etpAdvertService.getEtpAdvertById(id));
    }

    /**
     * 新增企业广告
     * @param etpAdvert 企业广告
     * @return R
     */
    @ApiOperation(value = "新增企业广告", notes = "新增企业广告")
    @SysLog("新增企业广告" )
    @PostMapping
    public R save(@RequestBody EtpAdvert etpAdvert) {
        return etpAdvertService.saveEtpAdvert(etpAdvert);
    }

    /**
     * 修改企业广告
     * @param etpAdvert 企业广告
     * @return R
     */
    @ApiOperation(value = "修改企业广告", notes = "修改企业广告")
    @SysLog("修改企业广告" )
    @PutMapping
    public R updateById(@RequestBody EtpAdvert etpAdvert) {
        return R.ok(etpAdvertService.updateById(etpAdvert));
    }

    /**
     * 通过id删除企业广告
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除企业广告", notes = "通过id删除企业广告")
    @DeleteMapping("/remove/{id}" )
    public R removeById(@PathVariable("id") Integer id) {
        return etpAdvertService.delEtpAdvert(id);
    }
    @ApiOperation(value = "点击率", notes = "点击率")
    @GetMapping("/addHits/{id}")
    public R addHits(@PathVariable("id") Integer id){
        return etpAdvertService.addHits(id);
    }
    @ApiOperation(value = "测试", notes = "测试")
    @GetMapping("/test/{id}")
    public R test(@PathVariable("id") Integer id){
        return etpAdvertService.test(id);
    }
}
