package com.htstar.ovms.codegen.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htstar.ovms.codegen.entity.GenConfig;
import com.htstar.ovms.codegen.entity.GenFormConf;
import com.htstar.ovms.codegen.mapper.GenFormConfMapper;
import com.htstar.ovms.codegen.mapper.GeneratorMapper;
import com.htstar.ovms.codegen.service.GeneratorService;
import com.htstar.ovms.codegen.util.GenUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author ovms
 * @date 2018-07-30
 * <p>
 * 代码生成器
 */
@Service
@AllArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {
	private final GeneratorMapper generatorMapper;
	private final GenFormConfMapper genFormConfMapper;

	/**
	 * 分页查询表
	 *
	 * @param tableName 查询条件
	 * @param dsName
	 * @return
	 */
	@Override
	@DS("#last")
	public IPage<List<Map<String, Object>>> getPage(Page page, String tableName, String dsName) {
		return generatorMapper.queryList(page, tableName);
	}

	/**
	 * 生成代码
	 *
	 * @param genConfig 生成配置
	 * @return
	 */
	@Override
	public byte[] generatorCode(GenConfig genConfig) {
		//根据tableName 查询最新的表单配置
		List<GenFormConf> formConfList = genFormConfMapper.selectList(Wrappers.<GenFormConf>lambdaQuery()
				.eq(GenFormConf::getTableName, genConfig.getTableName())
				.orderByDesc(GenFormConf::getCreateTime));

		DynamicDataSourceContextHolder.push(genConfig.getDsName());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		String tableNames = genConfig.getTableName();
		for (String tableName : StrUtil.split(tableNames, StrUtil.DASHED)) {
			//查询表信息
			Map<String, String> table = generatorMapper.queryTable(tableName,genConfig.getDsName());
			//查询列信息
			List<Map<String, String>> columns = generatorMapper.queryColumns(tableName, genConfig.getDsName());
			//生成代码
			if (CollUtil.isNotEmpty(formConfList)) {
				GenUtils.generatorCode(genConfig, table, columns, zip, formConfList.get(0));
			} else {
				GenUtils.generatorCode(genConfig, table, columns, zip, null);
			}
		}
		IoUtil.close(zip);
		return outputStream.toByteArray();
	}
}
