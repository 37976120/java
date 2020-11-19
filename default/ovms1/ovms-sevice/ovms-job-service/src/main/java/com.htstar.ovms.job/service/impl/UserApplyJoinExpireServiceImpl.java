package com.htstar.ovms.job.service.impl;

import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.job.mapper.UserApplyJoinExpireMapper;
import com.htstar.ovms.job.service.UserApplyJoinExpireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/9/4
 * Company: 航通星空
 * Modified By:
 */
@Service
public class UserApplyJoinExpireServiceImpl implements UserApplyJoinExpireService {

    @Autowired
    private UserApplyJoinExpireMapper userApplyJoinExpireMapper;

    @Override
    public boolean expireUserApplyJoin() {
        LocalDate now = OvmDateUtil.getCstNowDate();
        userApplyJoinExpireMapper.expireUserApplyJoinByDate(now);
        return true;
    }
}
