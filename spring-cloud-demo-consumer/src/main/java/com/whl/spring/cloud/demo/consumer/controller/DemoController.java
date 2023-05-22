package com.whl.spring.cloud.demo.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/demo")
public class DemoController {
    private static final String SERVICE_NAME = "spring-cloud-demo-provider";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = {"", "/"})
    public String index() {
        String path = String.format("http://%s/demo", SERVICE_NAME);
        System.out.println("request path:" + path);
        return this.restTemplate.getForObject(path, String.class);
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        String path = String.format("http://%s/demo/test", SERVICE_NAME);
        System.out.println("request path:" + path);
        ResponseEntity<String> entity = this.restTemplate.getForEntity(path, String.class);
        String value = entity.getHeaders().getFirst("test");
        response.setHeader("test", value);
        return entity.getBody();
    }

}
