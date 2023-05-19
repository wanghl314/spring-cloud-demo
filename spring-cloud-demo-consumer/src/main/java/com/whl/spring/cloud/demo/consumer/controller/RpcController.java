package com.whl.spring.cloud.demo.consumer.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whl.spring.cloud.demo.DemoService;
import com.whl.spring.cloud.demo.UserService;

@RestController
@RequestMapping("/rpc")
public class RpcController {
    @DubboReference
    private UserService userService;

    @DubboReference(group = "test")
    private DemoService demoService;

    @GetMapping("/user")
    public String sayHello(String name) {
        this.userService.sayHello(name);
        return "SUCCESS";
    }

    @GetMapping("/demo")
    public String test(String name) {
        return this.demoService.test(name);
    }

    @GetMapping("/demo2")
    public String test2(String name) {
        return this.demoService.test2(name);
    }

    @GetMapping("/demo3")
    public String test3(String name) {
        return this.demoService.test3(name);
    }

}
