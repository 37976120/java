
package com.htstar.ovms.mp.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.data.tenant.TenantContextHolder;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.mp.entity.WxFansMsg;
import com.htstar.ovms.mp.service.WxFansMsgService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 微信粉丝消息管理
 *
 * @author ovms
 * @date 2019-03-27 20:45:27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wxfansmsg")
public class WxFansMsgController {

	private final WxFansMsgService wxFansMsgService;

	/**
	 * 分页查询
	 *
	 * @param page      分页对象
	 * @param wxFansMsg 消息
	 * @return
	 */
	@GetMapping("/page")
	public R getWxFansMsgPage(Page page, WxFansMsg wxFansMsg) {
		wxFansMsg.setEtpId(TenantContextHolder.getEtpId());
		return R.ok(wxFansMsgService.page(page, Wrappers.query(wxFansMsg)));
	}


	/**
	 * 通过id查询消息
	 *
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Integer id) {
		return R.ok(wxFansMsgService.getById(id));
	}

	/**
	 * 新增消息
	 *
	 * @param wxFansMsg 消息
	 * @return R
	 */
	@SysLog("新增消息")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('mp_wxfansmsg_add')")
	public R save(@RequestBody WxFansMsg wxFansMsg) {
		wxFansMsg.setEtpId(TenantContextHolder.getEtpId());
		return R.ok(wxFansMsgService.save(wxFansMsg));
	}

	/**
	 * 修改消息
	 *
	 * @param wxFansMsg 消息
	 * @return R
	 */
	@SysLog("修改消息")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('mp_wxfansmsg_edit')")
	public R updateById(@RequestBody WxFansMsg wxFansMsg) {
		wxFansMsg.setEtpId(TenantContextHolder.getEtpId());
		return R.ok(wxFansMsgService.updateById(wxFansMsg));
	}

	/**
	 * 通过id删除消息
	 *
	 * @param id id
	 * @return R
	 */
	@SysLog("删除消息")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('mp_wxfansmsg_del')")
	public R removeById(@PathVariable Integer id) {
		return R.ok(wxFansMsgService.removeById(id));
	}

}
