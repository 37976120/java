package com.htstar.ovms.msg.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.htstar.ovms.msg.api.entity.MsgPushLog;
import com.htstar.ovms.msg.api.vo.MsgPushLogCountVO;

import java.util.List;

/**
 * 消息推送记录
 *
 * @author htxk
 * @date 2020-08-14 16:10:21
 */
public interface MsgPushLogService extends IService<MsgPushLog> {
    /**
     * 三种消息总数
     * @return
     */
   List<MsgPushLogCountVO> getMsgPushLogCount(Integer userId);
}
