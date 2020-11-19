
package com.htstar.ovms.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.service.OvmsUser;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.mp.config.WxMpConfiguration;
import com.htstar.ovms.mp.entity.WxFansMsg;
import com.htstar.ovms.mp.entity.WxFansMsgRes;
import com.htstar.ovms.mp.mapper.WxFansMsgMapper;
import com.htstar.ovms.mp.mapper.WxFansMsgResMapper;
import com.htstar.ovms.mp.service.WxFansMsgResService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 微信消息回复业务
 *
 * @author ovms
 * @date 2019-03-27 20:45:48
 */
@Slf4j
@Service
@AllArgsConstructor
public class WxFansMsgResServiceImpl extends ServiceImpl<WxFansMsgResMapper, WxFansMsgRes> implements WxFansMsgResService {
	private final WxFansMsgMapper wxFansMsgMapper;


	/**
	 * 保存并发送回复
	 * <p>
	 * 1. 根据消息ID 查询消息的所属 公众号信息
	 * 2. 构建文本客服消息并发送
	 * 3. 完善回复消息并保存
	 * 4. 更新微信消息为已回复
	 *
	 * @param wxFansMsgRes
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R saveAndSend(WxFansMsgRes wxFansMsgRes) {
		WxFansMsg wxFansMsg = wxFansMsgMapper.selectById(wxFansMsgRes.getFansMsgId());

		String wxAccountAppid = wxFansMsg.getWxAccountAppid();
		WxMpService wxMpService = WxMpConfiguration.getMpServices().get(wxAccountAppid);

		try {
			wxMpService.getKefuService().sendKefuMessage(WxMpKefuMessage
					.TEXT()
					.toUser(wxFansMsg.getOpenid())
					.content(wxFansMsgRes.getResContent())
					.build());
		} catch (WxErrorException e) {
			log.error("发送客服消息失败 ", e);
			return R.failed(e.getError().getErrorMsg());
		}

		OvmsUser user = SecurityUtils.getUser();
		wxFansMsgRes.setUserId(user.getId());
		wxFansMsgRes.setUserName(user.getUsername());
		baseMapper.insert(wxFansMsgRes);

		wxFansMsg.setIsRes("1");
		wxFansMsgMapper.updateById(wxFansMsg);
		return R.ok();
	}
}
