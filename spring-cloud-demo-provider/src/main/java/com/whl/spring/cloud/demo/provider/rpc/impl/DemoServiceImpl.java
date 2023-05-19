package com.whl.spring.cloud.demo.provider.rpc.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import com.whl.spring.cloud.demo.DemoService;

@Service("demoServiceImpl")
public class DemoServiceImpl implements DemoService {

    @Override
    public String test(String name) {
        return "Hello " + name + ", this time is " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @Override
    public String test2(String name) {
        return "Hello " + name + ", random long is " + RandomUtils.nextLong();
    }

    @Override
    public String test3(String name) {
        return "Hello " + name + ", random string is " + RandomStringUtils.randomAlphanumeric(10);
    }

}
