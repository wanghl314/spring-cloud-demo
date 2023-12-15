package com.whl.spring.cloud.demo;

import com.whl.spring.cloud.demo.sentinel.annotation.AddSentinel;
import com.whl.spring.cloud.demo.sentinel.annotation.DemoSentinelResource;
import com.whl.spring.cloud.demo.service.DemoServiceImpl;

@DemoSentinelResource
public class DemoServiceImpl1 extends DemoServiceImpl {

    @Override
    @AddSentinel
    public String test(String name) {
        return "test all";
    }

}
