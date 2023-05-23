package com.whl.spring.cloud.demo.consumer.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;

@Configuration
public class ConsumerConfig {

    @Bean
    @LoadBalanced
    @SentinelRestTemplate(blockHandler = "handleException", blockHandlerClass = ExceptionUtil.class, fallback = "handleFallback", fallbackClass = ExceptionUtil.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
