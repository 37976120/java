package com.htstar.ovms.enterprise.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.CommonConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.enterprise.api.entity.EtpAdvert;
import com.htstar.ovms.enterprise.api.req.NoticeReq;
import com.htstar.ovms.enterprise.mapper.EtpAdvertMapper;
import com.htstar.ovms.enterprise.service.EtpAdvertService;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.BAD_CONTEXT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * 企业广告
 *
 * @author lw
 * @date 2020-08-10 17:29:10
 */
@Service
@Slf4j
public class EtpAdvertServiceImpl extends ServiceImpl<EtpAdvertMapper, EtpAdvert> implements EtpAdvertService {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 新增
     *
     * @param advert
     * @return
     */
    @Override
    public R saveEtpAdvert(EtpAdvert advert) {
        OvmsUser user = SecurityUtils.getUser();
        Integer userId = user.getId();
        Integer etpId = user.getEtpId();
        advert.setEtpId(etpId);
        advert.setPutUserId(userId);
        baseMapper.insert(advert);
        return R.ok();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public R delEtpAdvert(Integer id) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(id.toString(), redisTemplate.getConnectionFactory());
        //点击率
        Integer increment = entityIdCounter.intValue();
        redisTemplate.delete(CacheConstants.ADVERT+id.toString());
        baseMapper.delEtpAdvert(id, increment);
        return R.ok("删除成功");
    }

    /**
     * 分页
     *
     * @param req
     * @return
     */
    @Override
    public IPage<EtpAdvert> queryPage(NoticeReq req) {
        Integer etpId = SecurityUtils.getUser().getEtpId();
        if (req.getEtpId() == null && etpId != CommonConstants.ETP_ID_1) {
            req.setEtpId(etpId);
        }
        IPage<EtpAdvert> etpAdvertIPage = baseMapper.queryPage(req);
        List<EtpAdvert> records = etpAdvertIPage.getRecords();
        ArrayList<EtpAdvert> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(records)) {
            for (EtpAdvert advert : records) {
                Integer id = advert.getId();
                RedisAtomicLong entityIdCounter = new RedisAtomicLong(CacheConstants.ADVERT+id.toString(), redisTemplate.getConnectionFactory());
                //点击率
                int increment = entityIdCounter.intValue();
                advert.setClickCount(increment + advert.getClickCount());
                list.add(advert);
            }
            etpAdvertIPage.setRecords(list);
        }
        return etpAdvertIPage;
    }

    @Override
    public EtpAdvert getEtpAdvertById(Integer id) {
        return baseMapper.getEtpAdvertById(id);
    }

    @Override
    public R addHits(Integer id) {
        String key = CacheConstants.ADVERT+id.toString();
        RedisAtomicInteger redisAtomicInteger = new RedisAtomicInteger(key, redisTemplate.getConnectionFactory());
        Integer clickCount = redisAtomicInteger.incrementAndGet();
        //超出50次,数据入库一次
        if (clickCount > 100) {
            baseMapper.updateClickCount(id, clickCount);
            redisTemplate.delete(key);
        }

        return R.ok(clickCount);
    }

    @Override
    public R test(Integer id) {
        RedisAtomicInteger entityIdCounter = new RedisAtomicInteger(id.toString(), redisTemplate.getConnectionFactory());
        int increment = entityIdCounter.intValue();
        log.info("当前数据为{}", increment);
        List<String> list2 = baseMapper.getList();
        List list1 = redisTemplate.opsForValue().multiGet(list2);
        return R.ok(list1);
    }
}
