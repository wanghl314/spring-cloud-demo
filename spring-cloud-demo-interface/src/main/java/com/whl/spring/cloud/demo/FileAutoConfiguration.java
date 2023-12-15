package com.whl.spring.cloud.demo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@ConditionalOnBean(RestTemplate.class)
@Configuration
public class FileAutoConfiguration {

    @Bean
    public FileService fileService(RestTemplate restTemplate) {
        return new FileServiceImpl(restTemplate);
    }

}
