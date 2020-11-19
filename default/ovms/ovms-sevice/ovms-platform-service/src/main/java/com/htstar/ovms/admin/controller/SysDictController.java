package com.htstar.ovms.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.admin.api.entity.SysDict;
import com.htstar.ovms.admin.api.entity.SysDictItem;
import com.htstar.ovms.admin.service.SysDictItemService;
import com.htstar.ovms.admin.service.SysDictService;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author ovms
 * @since 2019-03-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/dict")
@Api(value = "dict", tags = "字典管理模块")
public class SysDictController {
	private final SysDictService sysDictService;
	private final SysDictItemService sysDictItemService;

	/**
	 * 通过ID查询字典信息
	 *
	 * @param id ID
	 * @return 字典信息
	 */
	@ApiOperation(value = "通过ID查询字典信息", notes = "通过ID查询字典信息")
	@GetMapping("/{id}")
	public R getById(@PathVariable Integer id) {
		return R.ok(sysDictService.getById(id));
	}

	/**
	 * 分页查询字典信息
	 *
	 * @param page 分页对象
	 * @return 分页对象
	 */
	@ApiOperation(value = "分页查询字典信息", notes = "分页查询字典信息")
	@GetMapping("/page")
	public R<IPage> getDictPage(Page page, SysDict sysDict) {
		return R.ok(sysDictService.page(page, Wrappers.query(sysDict)));
	}

	/**
	 * 通过字典类型查找字典
	 *
	 * @param type 类型
	 * @return 同类型字典
	 */
	@ApiOperation(value = "通过字典类型查找字典", notes = "通过字典类型查找字典")
	@GetMapping("/type/{type}")
	@Cacheable(value = CacheConstants.DICT_DETAILS, key = "#type", unless = "#result.data.isEmpty()")
	public R getDictByType(@PathVariable String type) {
		return R.ok(sysDictItemService.list(Wrappers
				.<SysDictItem>query().lambda()
				.eq(SysDictItem::getType, type)));
	}

	/**
	 * 添加字典
	 *
	 * @param sysDict 字典信息
	 * @return success、false
	 */
	@SysLog("添加字典")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_dict_add')")
	@ApiOperation(value = "添加字典", notes = "添加字典")
	public R save(@Valid @RequestBody SysDict sysDict) {
		if (null != sysDict&& sysDict.getType()!= null){
			sysDict.setType(sysDict.getType().trim());
		}
		return R.ok(sysDictService.save(sysDict));

	}

	/**
	 * 删除字典，并且清除字典缓存
	 *
	 * @param id ID
	 * @return R
	 */
	@SysLog("删除字典")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_dict_del')")
	@ApiOperation(value = "删除字典", notes = "删除字典")
	public R removeById(@PathVariable Integer id) {
		return sysDictService.removeDict(id);
	}

	/**
	 * 修改字典
	 *
	 * @param sysDict 字典信息
	 * @return success/false
	 */
	@ApiOperation(value = "修改字典", notes = "修改字典")
	@PutMapping
	@SysLog("修改字典")
	@PreAuthorize("@pms.hasPermission('sys_dict_edit')")
	public R updateById(@Valid @RequestBody SysDict sysDict) {
		return sysDictService.updateDict(sysDict);
	}

	/**
	 * 分页查询
	 *
	 * @param page        分页对象
	 * @param sysDictItem 字典项
	 * @return
	 */
	@ApiOperation(value = "分页对象", notes = "字典项")
	@GetMapping("/item/page")
	public R getSysDictItemPage(Page page, SysDictItem sysDictItem) {
		return R.ok(sysDictItemService.page(page, Wrappers.query(sysDictItem)));
	}


	/**
	 * 通过id查询字典项
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "通过id查询字典项", notes = "通过id查询字典项")
	@GetMapping("/item/{id}")
	public R getDictItemById(@PathVariable("id") Integer id) {
		return R.ok(sysDictItemService.getById(id));
	}

	/**
	 * 新增字典项
	 *
	 * @param sysDictItem 字典项
	 * @return R
	 */
	@ApiOperation(value = "新增字典项", notes = "新增字典项")
	@SysLog("新增字典项")
	@PostMapping("/item")
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	public R save(@RequestBody SysDictItem sysDictItem) {
		return R.ok(sysDictItemService.save(sysDictItem));
	}

	/**
	 * 修改字典项
	 *
	 * @param sysDictItem 字典项
	 * @return R
	 */
	@ApiOperation(value = "修改字典项", notes = "修改字典项")
	@SysLog("修改字典项")
	@PutMapping("/item")
	public R updateById(@RequestBody SysDictItem sysDictItem) {
		return sysDictItemService.updateDictItem(sysDictItem);
	}

	/**
	 * 通过id删除字典项
	 *
	 * @param id id
	 * @return R
	 */
	@ApiOperation(value = "删除字典项", notes = "删除字典项")
	@SysLog("删除字典项")
	@DeleteMapping("/item/{id}")
	public R removeDictItemById(@PathVariable Integer id) {
		return sysDictItemService.removeDictItem(id);
	}
}
