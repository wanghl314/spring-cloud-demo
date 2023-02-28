package com.whl.spring.cloud.demo.consumer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = {"", "/"})
    public String index() {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("spring-cloud-demo-provider");
        String path = String.format("http://%s:%s/demo", serviceInstance.getHost(), serviceInstance.getPort());
        System.out.println("request path:" + path);
        return this.restTemplate.getForObject(path, String.class);
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("spring-cloud-demo-provider");
        String path = String.format("http://%s:%s/demo/test", serviceInstance.getHost(), serviceInstance.getPort());
        System.out.println("request path:" + path);
        ResponseEntity<String> entity = this.restTemplate.getForEntity(path, String.class);
        String value = entity.getHeaders().getFirst("test");
        response.setHeader("test", value);
        return entity.getBody();
    }

}
