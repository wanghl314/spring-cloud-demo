package com.whl.spring.cloud.demo.provider.rpc.impl;

import org.apache.dubbo.config.annotation.DubboService;

import com.whl.spring.cloud.demo.UserService;

@DubboService
public class UserServiceImpl implements UserService {

    @Override
    public void sayHello(String name) {
        System.out.println("Hello, " + name);
    }

}
