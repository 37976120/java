package com.htstar.ovms.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.htstar.ovms.admin.api.entity.SysUser;
import com.htstar.ovms.admin.mapper.SysUserMapper;
import com.htstar.ovms.admin.service.MobileService;
import com.htstar.ovms.admin.service.SmsService;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.common.core.constant.enums.LoginTypeEnum;
import com.htstar.ovms.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 手机短信
 */
@Slf4j
@Service
@RefreshScope
@AllArgsConstructor
public class MobileServiceImpl implements MobileService {
	private final RedisTemplate redisTemplate;
	private final SysUserMapper userMapper;
	private final SmsService smsService;

	/**
	 * 发送手机验证码
	 * @param mobile mobile
	 * @return code
	 */
	@Override
	public R<Boolean> sendSmsCodeForExistUser(String mobile) {
		List<SysUser> userList = userMapper.selectList(Wrappers
			.<SysUser>query().lambda()
			.eq(SysUser::getPhone, mobile));

		if (CollUtil.isEmpty(userList)) {
			log.info("手机号未注册:{}", mobile);
			return R.ok(Boolean.FALSE, "手机号未注册");
		}

		Object codeObj = redisTemplate.opsForValue().get(CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + StringPool.AT + mobile);

		if (codeObj != null) {
			log.info("手机号验证码未过期:{}，{}", mobile, codeObj);
			return R.ok(Boolean.FALSE, "验证码发送过于频繁，请稍后。");
		}

		String code = RandomUtil.randomNumbers(Integer.parseInt(SecurityConstants.CODE_SIZE));

		if (smsService.sendAliSms(mobile, code)){
			redisTemplate.opsForValue().set(
					CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + StringPool.AT + mobile
					, code, SecurityConstants.CODE_TIME, TimeUnit.SECONDS);
			return R.ok(Boolean.TRUE, code);
		}else {
			return R.ok(Boolean.FALSE, "短信通道异常");
		}
	}


}
