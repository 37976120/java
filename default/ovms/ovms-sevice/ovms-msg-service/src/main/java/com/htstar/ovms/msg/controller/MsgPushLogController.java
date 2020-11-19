package com.htstar.ovms.msg.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.msg.api.constant.MsgTypeConstant;
import com.htstar.ovms.msg.api.constant.UserIDConstant;
import com.htstar.ovms.msg.api.dto.MsgPushLogDTO;
import com.htstar.ovms.msg.api.entity.MsgPushLog;
import com.htstar.ovms.msg.api.vo.MsgPushLogCountVO;
import com.htstar.ovms.msg.service.MsgPushLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 消息推送记录
 *
 * @author htxk
 * @date 2020-08-14 16:10:21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/msgpushlog")
@Api(value = "msgpushlog", tags = "消息推送记录管理")
@Slf4j
public class MsgPushLogController {

    private final MsgPushLogService msgPushLogService;

    /**
     * 分页查询
     *
     * @param msgPushLogDTO
     * @return
     */
    @ApiOperation(value = "分页查询按照类型查询消息", notes = "分页查询按照类型查询消息")
    @PostMapping("/page")
    public R getMsgPushLogPage(@RequestBody MsgPushLogDTO msgPushLogDTO) {
        QueryWrapper<MsgPushLog> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MsgPushLog::getMsgType, msgPushLogDTO.getMsgType());
        wrapper.lambda().eq(MsgPushLog::getUserId, msgPushLogDTO.getUserId());
        wrapper.lambda().orderByDesc(MsgPushLog::getCreateTime);
        return R.ok(msgPushLogService.page(msgPushLogDTO, wrapper));
    }


    /**
     * 通过id查询消息推送记录
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(msgPushLogService.getById(id));
    }

    /**
     * 通过消息类型查询
     *
     * @param msgtype
     * @return R
     */
    @ApiOperation(value = "通过消息类型查询", notes = "通过消息类型查询")
    @GetMapping("/msgpush/{msgtype}")
    public R getmsgType(@PathVariable("msgtype") Integer msgtype) {
        QueryWrapper<MsgPushLog> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MsgPushLog::getMsgType, msgtype);
        return R.ok(msgPushLogService.list(wrapper));
    }

    /**
     * 修改消息推送记录
     *
     * @param id 修改消息标记已读
     * @return R
     */
    @ApiOperation(value = "修改消息标记已读", notes = "修改消息标记已读")
    @GetMapping("/statusRead/{id}/{userId}")
    public R updateStatusUserIdById(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId){
        UpdateWrapper<MsgPushLog> wrapper = null;
        try {
            log.info("修改消息状态消息id{}",id);
            wrapper = new UpdateWrapper<>();
            wrapper.lambda().eq(MsgPushLog::getId, id);
            wrapper.lambda().eq(MsgPushLog::getUserId, userId);
            wrapper.lambda().set(MsgPushLog::getMsgStatus, MsgTypeConstant.MSG_STATUS_READ);
        } catch (Exception e) {
            log.error("修改消息标记已读--> 获取消息id为空");
        } finally {
            return R.ok(msgPushLogService.update(wrapper));
        }

    }

    /**
     * 新增消息推送记录
     *
     * @param msgPushLog 消息推送记录
     * @return R
     */
    @ApiOperation(value = "新增消息推送记录", notes = "新增消息推送记录")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('msg_msgpushlog_add')")
    public R save(@RequestBody MsgPushLog msgPushLog) {
        return R.ok(msgPushLogService.save(msgPushLog));
    }

    /**
     * 修改消息推送记录
     *
     * @param msgPushLog 消息推送记录
     * @return R
     */
    @ApiOperation(value = "修改消息推送记录", notes = "修改消息推送记录")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('msg_msgpushlog_edit')")
    public R updateById(@RequestBody MsgPushLog msgPushLog) {
        return R.ok(msgPushLogService.updateById(msgPushLog));
    }

    /**
     * 通过id删除消息推送记录
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除消息推送记录", notes = "通过id删除消息推送记录")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('msg_msgpushlog_del')")
    public R removeById(@PathVariable Integer id) {
        return R.ok(msgPushLogService.removeById(id));
    }

    /**
     * 查询不同消息类型未读总数
     *
     * @return R
     */
    @ApiOperation(value = "查询不同消息类型未读总数", notes = "查询不同消息类型未读总数")
    @GetMapping("/getMsgPushLogCount/{userId}")
    public List<MsgPushLogCountVO> getMsgPushLogCount(@PathVariable("userId") Integer userId) {
        return msgPushLogService.getMsgPushLogCount(userId);
    }

}
