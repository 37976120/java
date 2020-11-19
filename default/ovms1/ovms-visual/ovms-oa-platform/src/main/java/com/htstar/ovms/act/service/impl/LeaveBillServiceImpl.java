package com.htstar.ovms.act.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.act.entity.LeaveBill;
import com.htstar.ovms.act.mapper.LeaveBillMapper;
import com.htstar.ovms.act.service.LeaveBillService;
import org.springframework.stereotype.Service;

/**
 * @author ovms
 * @date 2018-09-27
 */
@Service("leaveBillService")
public class LeaveBillServiceImpl extends ServiceImpl<LeaveBillMapper, LeaveBill> implements LeaveBillService {

}
