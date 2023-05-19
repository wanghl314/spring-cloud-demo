package com.whl.spring.cloud.demo;

import com.whl.spring.cloud.demo.annotation.AddSentinel;
import com.whl.spring.cloud.demo.annotation.SentinelResource;

@SentinelResource
public class DemoServiceImpl1 extends DemoServiceImpl {

    @Override
    @AddSentinel
    public String test(String name) {
        return "test all";
    }

}
