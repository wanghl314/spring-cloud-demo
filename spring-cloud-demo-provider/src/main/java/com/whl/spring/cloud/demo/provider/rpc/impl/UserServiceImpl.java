package com.whl.spring.cloud.demo.provider.rpc.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whl.spring.cloud.demo.UserService;

@DubboService
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void sayHello(String name) {
        logger.info("Hello, {}", name);
    }

}
