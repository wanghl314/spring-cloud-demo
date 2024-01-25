package com.whl.spring.cloud.demo.consumer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;

@RestController
@RequestMapping("/demo")
public class DemoController {
    private static final String SERVICE_NAME = "spring-cloud-demo-provider";

    private static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestClient restClient;

    @GetMapping(value = {"", "/"})
    public String index() throws Exception {
        String path = String.format("http://%s/demo", SERVICE_NAME);
        logger.info("request path: {}", path);
        return this.restTemplate.getForObject(path, String.class);
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = String.format("http://%s/demo/test", SERVICE_NAME);
        logger.info("request path: {}", path);
        ResponseEntity<String> entity = this.restTemplate.getForEntity(path, String.class);
        String value = entity.getHeaders().getFirst("test");
        response.setHeader("test", value);
        return entity.getBody();
    }

    @GetMapping("/test2")
    public String test2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = String.format("http://%s/demo/test", SERVICE_NAME);
        logger.info("request path: {}", path);
        return this.restClient.get().uri(path).exchange((req, resp) -> {
            String value = resp.getHeaders().getFirst("test");
            response.setHeader("test", value);
            return Objects.requireNonNull(resp.bodyTo(String.class));
        });
    }

    @GetMapping("/testRestTemplateDegrade")
    public String testRestTemplateDegrade(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = String.format("http://%s/demo/testRestTemplateDegrade", SERVICE_NAME);
        logger.info("request path: {}", path);
        ResponseEntity<String> entity = this.restTemplate.getForEntity(path, String.class);
        String value = entity.getHeaders().getFirst("test");
        response.setHeader("test", value);
        return entity.getBody();
    }

}
