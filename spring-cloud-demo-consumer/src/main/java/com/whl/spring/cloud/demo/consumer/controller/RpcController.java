package com.whl.spring.cloud.demo.consumer.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whl.spring.cloud.demo.UserService;

@RestController
@RequestMapping("/rpc")
public class RpcController {
    @DubboReference
    private UserService userService;

    @GetMapping("/user")
    public String sayHello(String name) {
        this.userService.sayHello(name);
        return "SUCCESS";
    }

}
