package com.whl.spring.cloud.demo.provider.rpc.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.whl.spring.cloud.demo.DemoService;

@Service("demoServiceImpl")
public class DemoServiceImpl implements DemoService {

    @SentinelResource(value = "demoService#test", blockHandler = "handlerTest")
    @Override
    public String test(String name) {
        return "Hello " + name + ", this time is " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @SentinelResource(value = "demoService#test2", blockHandler = "handlerTest2")
    @Override
    public String test2(String name) {
        return "Hello " + name + ", random long is " + RandomUtils.nextLong();
    }

    @SentinelResource(value = "demoService#test3", blockHandler = "handlerTest3")
    @Override
    public String test3(String name) {
        return "Hello " + name + ", random string is " + RandomStringUtils.randomAlphanumeric(10);
    }

    @Override
    public String testDegrade(String name) {
        throw new RuntimeException("固定返回异常信息");
    }

    public String handlerTest(String name, BlockException e) {
        return "DemoService#test: 请求过于频繁，请稍后再试。";
    }

    public String handlerTest2(String name, BlockException e) {
        return "DemoService#test2: 请求过于频繁，请稍后再试。";
    }

    public String handlerTest3(String name, BlockException e) {
        return "DemoService#test3: 请求过于频繁，请稍后再试。";
    }

}
