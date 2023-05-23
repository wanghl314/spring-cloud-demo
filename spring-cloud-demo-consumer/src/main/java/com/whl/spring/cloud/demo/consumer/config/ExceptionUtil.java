package com.whl.spring.cloud.demo.consumer.config;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;

public class ExceptionUtil {

    public static ClientHttpResponse handleException(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException e) {
        return new SentinelClientHttpResponse("触发降级保护");
    }

    public static ClientHttpResponse handleFallback(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException e) {
        return new SentinelClientHttpResponse("触发异常保护");
    }

}
