package com.htstar.ovms.pay.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import com.htstar.ovms.pay.entity.PayRefundOrder;
import com.htstar.ovms.pay.service.PayRefundOrderService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 退款
 *
 * @author ovms
 * @date 2019-05-28 23:58:11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/payrefundorder")
@Api(value = "payrefundorder", tags = "payrefundorder管理")
public class PayRefundOrderController {

	private final PayRefundOrderService payRefundOrderService;

	/**
	 * 分页查询
	 *
	 * @param page           分页对象
	 * @param payRefundOrder 退款
	 * @return
	 */
	@GetMapping("/page")
	public R getPayRefundOrderPage(Page page, PayRefundOrder payRefundOrder) {
		return R.ok(payRefundOrderService.page(page, Wrappers.query(payRefundOrder)));
	}


	/**
	 * 通过id查询退款
	 *
	 * @param refundOrderId id
	 * @return R
	 */
	@GetMapping("/{refund]orderId}")
	public R getById(@PathVariable("refund]orderId") String refundOrderId) {
		return R.ok(payRefundOrderService.getById(refundOrderId));
	}

	/**
	 * 新增退款
	 *
	 * @param payRefundOrder 退款
	 * @return R
	 */
	@SysLog("新增退款")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('pay_payrefundorder_add')")
	public R save(@RequestBody PayRefundOrder payRefundOrder) {
		return R.ok(payRefundOrderService.save(payRefundOrder));
	}

	/**
	 * 修改退款
	 *
	 * @param payRefundOrder 退款
	 * @return R
	 */
	@SysLog("修改退款")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('pay_payrefundorder_edit')")
	public R updateById(@RequestBody PayRefundOrder payRefundOrder) {
		return R.ok(payRefundOrderService.updateById(payRefundOrder));
	}

	/**
	 * 通过id删除退款
	 *
	 * @param refundOrderId refundOrderId
	 * @return R
	 */
	@SysLog("删除退款")
	@DeleteMapping("/{refundOrderId}")
	@PreAuthorize("@pms.hasPermission('pay_payrefundorder_del')")
	public R removeById(@PathVariable String refundOrderId) {
		return R.ok(payRefundOrderService.removeById(refundOrderId));
	}

}
