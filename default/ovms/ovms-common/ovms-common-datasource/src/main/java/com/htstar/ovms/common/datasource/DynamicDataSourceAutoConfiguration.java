package com.htstar.ovms.common.datasource;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.htstar.ovms.common.datasource.config.DruidDataSourceProperties;
import com.htstar.ovms.common.datasource.config.JdbcDynamicDataSourceProvider;
import com.htstar.ovms.common.datasource.config.LastParamDsProcessor;
import lombok.AllArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ovms
 * @date 2020-02-06
 * <p>
 * 动态数据源切换配置
 */
@Configuration
@AllArgsConstructor
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DruidDataSourceProperties.class)
public class DynamicDataSourceAutoConfiguration {
	private final StringEncryptor stringEncryptor;
	private final DruidDataSourceProperties properties;

	@Bean
	public DynamicDataSourceProvider dynamicDataSourceProvider() {
		return new JdbcDynamicDataSourceProvider(stringEncryptor, properties);
	}

	@Bean
	public DsProcessor dsProcessor() {
		return new LastParamDsProcessor();
	}
}
