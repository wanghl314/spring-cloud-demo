/*
 * Copyright 2013-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.whl.spring.cloud.demo.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.apache.seata.common.ConfigurationKeys.SEATA_PREFIX;

/**
 * @author xiaojing
 */
@ConditionalOnProperty(prefix = SEATA_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
public class SeataRestTemplateAutoConfiguration {

	@Bean
	public SeataRestTemplateInterceptor seataRestTemplateInterceptor() {
		return new SeataRestTemplateInterceptor();
	}

	@Bean
	public SeataRestTemplateInterceptorAfterPropertiesSet seataRestTemplateInterceptorConfiguration() {
		return new SeataRestTemplateInterceptorAfterPropertiesSet();
	}

}
