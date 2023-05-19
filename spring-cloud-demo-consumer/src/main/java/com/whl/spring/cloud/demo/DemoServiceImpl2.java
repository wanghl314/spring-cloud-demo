package com.whl.spring.cloud.demo;

import com.whl.spring.cloud.demo.annotation.AddSentinel;
import com.whl.spring.cloud.demo.annotation.SentinelResource;
import com.whl.spring.cloud.demo.sentinel.SentinelExceptionEnum;

@SentinelResource
public class DemoServiceImpl2 extends DemoServiceImpl {

    @Override
    @AddSentinel(exception = SentinelExceptionEnum.NETWORK)
    public String test2(String name) {
        return "network";
    }

}
