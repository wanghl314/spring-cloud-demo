package com.whl.spring.cloud.demo;

import com.whl.spring.cloud.demo.sentinel.annotation.AddSentinel;
import com.whl.spring.cloud.demo.sentinel.annotation.DemoSentinelResource;
import com.whl.spring.cloud.demo.sentinel.SentinelExceptionEnum;

@DemoSentinelResource
public class DemoServiceImpl2 extends DemoServiceImpl {

    @Override
    @AddSentinel(exception = SentinelExceptionEnum.NETWORK)
    public String test2(String name) {
        return "network";
    }

}
