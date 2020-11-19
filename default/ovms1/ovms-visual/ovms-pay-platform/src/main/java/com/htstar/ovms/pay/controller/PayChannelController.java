package com.htstar.ovms.pay.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.pay.config.PayConfigParmaInitRunner;
import com.htstar.ovms.pay.entity.PayChannel;
import com.htstar.ovms.pay.service.PayChannelService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 渠道
 *
 * @author ovms
 * @date 2019-05-28 23:57:58
 */
@RestController
@AllArgsConstructor
@RequestMapping("/paychannel")
@Api(value = "paychannel", tags = "paychannel管理")
public class PayChannelController {
	private final PayConfigParmaInitRunner parmaInitRunner;
	private final PayChannelService payChannelService;

	/**
	 * 分页查询
	 *
	 * @param page       分页对象
	 * @param payChannel 渠道
	 * @return
	 */
	@GetMapping("/page")
	public R getPayChannelPage(Page page, PayChannel payChannel) {
		return R.ok(payChannelService.page(page, Wrappers.query(payChannel)));
	}


	/**
	 * 通过id查询渠道
	 *
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(payChannelService.getById(id));
	}

	/**
	 * 新增渠道
	 *
	 * @param payChannel 渠道
	 * @return R
	 */
	@SysLog("新增渠道")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('pay_paychannel_add')")
	public R save(@RequestBody PayChannel payChannel) {
		payChannelService.saveChannel(payChannel);
		parmaInitRunner.initPayConfig();
		return R.ok();
	}

	/**
	 * 修改渠道
	 *
	 * @param payChannel 渠道
	 * @return R
	 */
	@SysLog("修改渠道")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('pay_paychannel_edit')")
	public R updateById(@RequestBody PayChannel payChannel) {
		payChannelService.updateById(payChannel);
		parmaInitRunner.initPayConfig();
		return R.ok();
	}

	/**
	 * 通过id删除渠道
	 *
	 * @param id id
	 * @return R
	 */
	@SysLog("删除渠道")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('pay_paychannel_del')")
	public R removeById(@PathVariable Integer id) {
		payChannelService.removeById(id);
		parmaInitRunner.initPayConfig();
		return R.ok();
	}

}
