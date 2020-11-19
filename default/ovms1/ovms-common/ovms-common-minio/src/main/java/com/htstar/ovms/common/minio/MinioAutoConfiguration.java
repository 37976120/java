package com.htstar.ovms.common.minio;

import com.htstar.ovms.common.minio.http.MinioEndpoint;
import com.htstar.ovms.common.minio.service.MinioTemplate;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * minio 自动配置类
 *
 * @author ovms
 */
@AllArgsConstructor
@EnableConfigurationProperties({MinioProperties.class})
public class MinioAutoConfiguration {
	private final MinioProperties properties;

	@Bean
	@ConditionalOnMissingBean(MinioTemplate.class)
	@ConditionalOnProperty(name = "minio.url")
	MinioTemplate template() {
		return new MinioTemplate(
				properties.getUrl(),
				properties.getAccessKey(),
				properties.getSecretKey()
		);
	}


	@Bean
	@ConditionalOnProperty(name = "minio.endpoint.enable", havingValue = "true")
	public MinioEndpoint minioEndpoint(MinioTemplate template) {
		return new MinioEndpoint(template);
	}

}
