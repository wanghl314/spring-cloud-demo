package com.whl.spring.cloud.demo.consumer.config;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExceptionUtil {

    public static ClientHttpResponse handleException(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException e) {
        return new SentinelClientHttpResponse("RestTemplate: 触发限流保护");
    }

    public static ClientHttpResponse handleFallback(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException e) {
        return new SentinelClientHttpResponse("RestTemplate: 触发降级保护");
    }

    /**
     * Sentinel暂未适配spring-6.2.0版本的临时解决方法
     */
    private static class SentinelClientHttpResponse implements ClientHttpResponse {

        private String blockResponse = "RestTemplate request block by sentinel";

        public SentinelClientHttpResponse() {
        }

        public SentinelClientHttpResponse(String blockResponse) {
            this.blockResponse = blockResponse;
        }

        @Override
        public HttpStatusCode getStatusCode() throws IOException {
            return HttpStatus.OK;
        }

        @Override
        public String getStatusText() throws IOException {
            return blockResponse;
        }

        @Override
        public void close() {
            // nothing do
        }

        @Override
        public InputStream getBody() throws IOException {
            return new ByteArrayInputStream(blockResponse.getBytes());
        }

        @Override
        public HttpHeaders getHeaders() {
            Map<String, List<String>> headers = new HashMap<>();
            headers.put(HttpHeaders.CONTENT_TYPE,
                    Arrays.asList(MediaType.APPLICATION_JSON_VALUE));
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.putAll(headers);
            return httpHeaders;
        }

    }

}
