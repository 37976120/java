
package com.htstar.ovms.mp.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.mp.config.WxMpConfiguration;
import com.htstar.ovms.mp.entity.WxAccount;
import com.htstar.ovms.mp.service.WxAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 公众号账户
 *
 * @author ovms
 * @date 2019-03-26 22:07:53
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wxaccount")
public class WxAccountController {
	private final WxMpConfiguration wxMpConfiguration;
	private final WxAccountService wxAccountService;

	/**
	 * 分页查询
	 *
	 * @param page      分页对象
	 * @param wxAccount 公众号账户
	 * @return
	 */
	@GetMapping("/page")
	public R getWxAccountPage(Page page, WxAccount wxAccount) {
		return R.ok(wxAccountService.page(page, Wrappers.query(wxAccount)));
	}


	/**
	 * 通过id查询公众号账户
	 *
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(wxAccountService.getById(id));
	}

	/**
	 * 新增公众号账户
	 *
	 * @param wxAccount 公众号账户
	 * @return R
	 */
	@SysLog("新增公众号账户")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('mp_wxaccount_add')")
	public R save(@RequestBody WxAccount wxAccount) {
		wxAccountService.save(wxAccount);
		wxMpConfiguration.initServices();
		return R.ok();
	}

	/**
	 * 修改公众号账户
	 *
	 * @param wxAccount 公众号账户
	 * @return R
	 */
	@SysLog("修改公众号账户")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('mp_wxaccount_edit')")
	public R updateById(@RequestBody WxAccount wxAccount) {
		wxAccountService.updateById(wxAccount);
		wxMpConfiguration.initServices();
		return R.ok();
	}

	/**
	 * 通过id删除公众号账户
	 *
	 * @param id id
	 * @return R
	 */
	@SysLog("删除公众号账户")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('mp_wxaccount_del')")
	public R removeById(@PathVariable Integer id) {
		wxAccountService.removeById(id);
		wxMpConfiguration.initServices();
		return R.ok();
	}

	/**
	 * 生成公众号二维码
	 *
	 * @param appId
	 * @return
	 */
	@SysLog("生成公众号二维码")
	@PostMapping("/qr/{appId}")
	@PreAuthorize("@pms.hasPermission('mp_wxaccount_add')")
	public R qr(@PathVariable String appId) {
		return wxAccountService.generateQr(appId);
	}

	/**
	 * 获取公众号列表
	 *
	 * @return
	 */
	@GetMapping("/list")
	public R list() {
		return R.ok(wxAccountService.list());
	}

	/**
	 * 获取公众号接口数据
	 *
	 * @param appId    公众号
	 * @param interval 时间间隔
	 * @return
	 */
	@GetMapping("/statistics")
	public R statistics(String appId, String interval) {
		return wxAccountService.statistics(appId, interval);
	}
}
