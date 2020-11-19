package com.htstar.ovms.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.entity.SysUserLice;
import com.htstar.ovms.admin.mapper.SysUserLiceMapper;
import com.htstar.ovms.admin.service.SysUserLiceService;
import com.htstar.ovms.common.core.util.R;
import org.omg.CORBA.BAD_CONTEXT;
import org.springframework.stereotype.Service;

/**
 * 用户驾驶证
 *
 * @author lw
 * @date 2020-07-08 10:32:23
 */
@Service
public class SysUserLiceServiceImpl extends ServiceImpl<SysUserLiceMapper, SysUserLice> implements SysUserLiceService {

    /**
     * 新增
     * @param sysUserLice
     * @return
     */
    @Override
    public R saveInfo(SysUserLice sysUserLice) {
        SysUserLice userLice = baseMapper.selectOne(new QueryWrapper<SysUserLice>().eq("user_id", sysUserLice.getUserId()));
        //原本存在数据
        if (userLice!=null){
            baseMapper.updateById(sysUserLice);
        }else {
            baseMapper.insert(sysUserLice);
        }
        return R.ok();
    }

    /**
     * 根据id获取
     * @param userId
     * @return
     */
    @Override
    public R<SysUserLice> getByUserId(Integer userId) {
        SysUserLice userLice = baseMapper.selectOne(new QueryWrapper<SysUserLice>().eq("user_id", userId));
        return R.ok(userLice);
    }
}
