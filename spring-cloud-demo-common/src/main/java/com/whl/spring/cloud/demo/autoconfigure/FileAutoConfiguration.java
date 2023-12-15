package com.whl.spring.cloud.demo.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.whl.spring.cloud.demo.service.FileService;
import com.whl.spring.cloud.demo.service.impl.FileServiceImpl;

@ConditionalOnBean(RestTemplate.class)
@Configuration
public class FileAutoConfiguration {

    @Bean
    public FileService fileService(RestTemplate restTemplate) {
        return new FileServiceImpl(restTemplate);
    }

}
