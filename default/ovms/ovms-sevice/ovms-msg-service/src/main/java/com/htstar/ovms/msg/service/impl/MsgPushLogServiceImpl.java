package com.htstar.ovms.msg.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.htstar.ovms.msg.api.entity.MsgPushLog;
import com.htstar.ovms.msg.api.vo.MsgPushLogCountVO;
import com.htstar.ovms.msg.mapper.MsgPushLogMapper;
import com.htstar.ovms.msg.service.MsgPushLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息推送记录
 *
 * @author htxk
 * @date 2020-08-14 16:10:21
 */
@Service
public class MsgPushLogServiceImpl extends ServiceImpl<MsgPushLogMapper, MsgPushLog> implements MsgPushLogService {

    /**
     * 三种消息总数
     * @return
     */
    @Override
    public List<MsgPushLogCountVO> getMsgPushLogCount(Integer userId) {
        return baseMapper.getMsgPushLogCount(userId);
    }
}
