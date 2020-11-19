
package com.htstar.ovms.mp.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.mp.entity.WxAccountFans;
import com.htstar.ovms.mp.service.WxAccountFansService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 微信公众号粉丝管理
 *
 * @author ovms
 * @date 2019-03-26 22:08:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wxaccountfans")
public class WxAccountFansController {

	private final WxAccountFansService wxAccountFansService;

	/**
	 * 分页查询
	 *
	 * @param page          分页对象
	 * @param wxAccountFans 微信公众号粉丝
	 * @return
	 */
	@GetMapping("/page")
	public R getWxAccountFansPage(Page page, WxAccountFans wxAccountFans) {
		return R.ok(wxAccountFansService.page(page, Wrappers.query(wxAccountFans)));
	}


	/**
	 * 通过id查询微信公众号粉丝
	 *
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(wxAccountFansService.getById(id));
	}

	/**
	 * 新增微信公众号粉丝
	 *
	 * @param wxAccountFans 微信公众号粉丝
	 * @return R
	 */
	@SysLog("新增微信公众号粉丝")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('mp_wxaccountfans_add')")
	public R save(@RequestBody WxAccountFans wxAccountFans) {
		return R.ok(wxAccountFansService.save(wxAccountFans));
	}

	/**
	 * 修改微信公众号粉丝
	 *
	 * @param wxAccountFans 微信公众号粉丝
	 * @return R
	 */
	@SysLog("修改微信公众号粉丝")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('mp_wxaccountfans_edit')")
	public R updateById(@RequestBody WxAccountFans wxAccountFans) {
		return R.ok(wxAccountFansService.updateById(wxAccountFans));
	}

	/**
	 * 通过id删除微信公众号粉丝
	 *
	 * @param id id
	 * @return R
	 */
	@SysLog("删除微信公众号粉丝")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('mp_wxaccountfans_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(wxAccountFansService.removeById(id));
	}

	/**
	 * 同步粉丝
	 *
	 * @param appId
	 * @return
	 */
	@PostMapping("/sync/{appId}")
	public R sync(@PathVariable String appId) {
		return R.ok(wxAccountFansService.syncAccountFans(appId));
	}

}