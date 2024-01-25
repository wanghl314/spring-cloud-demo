package com.whl.spring.cloud.demo.consumer.config;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.whl.spring.cloud.demo.service.FileServiceV2;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ConsumerConfig {

    @Bean
    @LoadBalanced
    @SentinelRestTemplate(blockHandler = "handleException", blockHandlerClass = ExceptionUtil.class, fallback = "handleFallback", fallbackClass = ExceptionUtil.class)
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RestClient restClient(RestTemplate restTemplate) {
        return RestClient.builder(restTemplate).build();
    }

    @Bean
    public FileServiceV2 fileServiceV2(RestClient restClient) {
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(FileServiceV2.class);
    }

}
