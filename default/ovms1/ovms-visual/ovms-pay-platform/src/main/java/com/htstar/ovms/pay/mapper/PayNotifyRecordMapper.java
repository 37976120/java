package com.htstar.ovms.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htstar.ovms.pay.entity.PayNotifyRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 异步通知记录
 *
 * @author ovms
 * @date 2019-05-28 23:57:23
 */
@Mapper
public interface PayNotifyRecordMapper extends BaseMapper<PayNotifyRecord> {

}
