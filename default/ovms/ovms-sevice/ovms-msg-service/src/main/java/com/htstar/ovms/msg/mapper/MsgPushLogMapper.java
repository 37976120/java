package com.htstar.ovms.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.msg.api.entity.MsgPushLog;
import com.htstar.ovms.msg.api.vo.MsgPushLogCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息推送记录
 *
 * @author htxk
 * @date 2020-08-14 16:10:21
 */
@Mapper
public interface MsgPushLogMapper extends BaseMapper<MsgPushLog> {
    /**
     * 三种消息总数
     * @return
     */
    List<MsgPushLogCountVO> getMsgPushLogCount(@Param("userId") Integer userId);
}
