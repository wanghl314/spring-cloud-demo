package com.whl.spring.cloud.demo.service;

import com.whl.spring.cloud.demo.sentinel.annotation.AddSentinel;
import com.whl.spring.cloud.demo.sentinel.annotation.DemoSentinelResource;

@DemoSentinelResource
public class DemoServiceImpl1 extends DemoServiceImpl {

    @Override
    @AddSentinel
    public String test(String name) {
        return "test all";
    }

}
