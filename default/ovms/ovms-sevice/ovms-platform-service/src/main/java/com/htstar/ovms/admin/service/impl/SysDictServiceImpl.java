
package com.htstar.ovms.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.admin.api.entity.SysDict;
import com.htstar.ovms.admin.api.entity.SysDictItem;
import com.htstar.ovms.admin.mapper.SysDictItemMapper;
import com.htstar.ovms.admin.mapper.SysDictMapper;
import com.htstar.ovms.admin.service.SysDictService;
import com.htstar.ovms.common.core.constant.CacheConstants;
import com.htstar.ovms.common.core.constant.enums.DictTypeEnum;
import com.htstar.ovms.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典表
 *
 * @author ovms
 * @date 2019/03/19
 */
@Service
@AllArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {
	private final SysDictItemMapper dictItemMapper;

	/**
	 * 根据ID 删除字典
	 *
	 * @param id 字典ID
	 * @return
	 */
	@Override
	@CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public R removeDict(Integer id) {
		SysDict dict = this.getById(id);
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystem())) {
			return R.failed("系统内置字典不能删除");
		}

		baseMapper.deleteById(id);
		dictItemMapper.delete(Wrappers.<SysDictItem>lambdaQuery()
				.eq(SysDictItem::getDictId, id));
		return R.ok();
	}

	/**
	 * 更新字典
	 *
	 * @param dict 字典
	 * @return
	 */
	@Override
	public R updateDict(SysDict dict) {
		SysDict sysDict = this.getById(dict.getId());
		// 系统内置
		if (DictTypeEnum.SYSTEM.getType().equals(sysDict.getSystem())) {
			return R.failed("系统内置字典不能修改");
		}
		return R.ok(this.updateById(dict));
	}
}
