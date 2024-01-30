package com.whl.spring.cloud.demo.autoconfigure;

import com.whl.spring.cloud.demo.service.FileService;
import com.whl.spring.cloud.demo.service.FileServiceHttpExchange;
import com.whl.spring.cloud.demo.service.impl.FileServiceHttpExchangeImpl;
import com.whl.spring.cloud.demo.service.impl.FileServiceRestTemplateImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class FileAutoConfiguration {

    @Configuration
    @ConditionalOnBean(RestTemplate.class)
    static class RestTemplateEngine {

        @Bean
        public FileService fileServiceRestTemplateImpl(RestTemplate restTemplate) {
            return new FileServiceRestTemplateImpl(restTemplate);
        }

        @Bean
        public FileService fileServiceHttpExchangeRestTemplateImpl(RestTemplate restTemplate) {
            RestTemplateAdapter adapter = RestTemplateAdapter.create(restTemplate);
            HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
            FileServiceHttpExchange httpExchange = factory.createClient(FileServiceHttpExchange.class);
            return new FileServiceHttpExchangeImpl(httpExchange);
        }

    }

    @Configuration
    @ConditionalOnClass(RestClient.class)
    static class RestClientEngine {

        @Bean
        @ConditionalOnBean(RestClient.Builder.class)
        @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
        public FileService fileServiceHttpExchangeRestClientImpl(RestClient.Builder restClientBuilder) {
            RestClient restClient = restClientBuilder.build();
            RestClientAdapter adapter = RestClientAdapter.create(restClient);
            HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
            FileServiceHttpExchange httpExchange = factory.createClient(FileServiceHttpExchange.class);
            return new FileServiceHttpExchangeImpl(httpExchange);
        }

    }

    @Configuration
    @ConditionalOnClass(WebClient.class)
    static class WebClientEngine {

        @Bean
        @ConditionalOnBean(WebClient.Builder.class)
        public FileService fileServiceHttpExchangeWebClientImpl(WebClient.Builder webClientBuilder) {
            WebClient webClient = webClientBuilder.build();
            WebClientAdapter adapter = WebClientAdapter.create(webClient);
            HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
            FileServiceHttpExchange httpExchange = factory.createClient(FileServiceHttpExchange.class);
            return new FileServiceHttpExchangeImpl(httpExchange);
        }

    }

}
