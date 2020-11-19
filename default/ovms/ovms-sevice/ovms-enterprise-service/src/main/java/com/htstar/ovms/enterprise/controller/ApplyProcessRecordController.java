package com.htstar.ovms.enterprise.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.enterprise.api.entity.ApplyProcessRecord;
import com.htstar.ovms.enterprise.api.vo.DetailApplyCarRecordVO;
import com.htstar.ovms.enterprise.service.ApplyProcessRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 流程记录
 *
 * @author flr
 * @date 2020-06-30 17:58:51
 */
@RestController
@AllArgsConstructor
@RequestMapping("/applyrecord" )
@Api(value = "applyrecord", tags = "流程记录")
public class ApplyProcessRecordController {

    private final  ApplyProcessRecordService applyProcessRecordService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param applyProcessRecord 流程记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getApplyProcessRecordPage(Page page, ApplyProcessRecord applyProcessRecord) {
        return R.ok(applyProcessRecordService.page(page, Wrappers.query(applyProcessRecord)));
    }


    @ApiOperation(value = "获取流程记录", notes = "获取流程记录")
    @GetMapping("/getRecord/{orderId}" )
    public R<List<DetailApplyCarRecordVO>> getRecord(@PathVariable Integer orderId) {
        return applyProcessRecordService.getRecord(orderId);
    }


    /**
     * 通过id删除流程记录
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除流程记录", notes = "通过id删除流程记录")
    @SysLog("通过id删除流程记录" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(applyProcessRecordService.removeById(id));
    }

}
