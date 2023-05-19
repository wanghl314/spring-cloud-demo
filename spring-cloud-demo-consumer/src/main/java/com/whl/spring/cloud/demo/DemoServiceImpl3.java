package com.whl.spring.cloud.demo;

import com.whl.spring.cloud.demo.annotation.AddSentinel;
import com.whl.spring.cloud.demo.annotation.SentinelResource;
import com.whl.spring.cloud.demo.sentinel.SentinelExceptionEnum;

@SentinelResource
public class DemoServiceImpl3 extends DemoServiceImpl {

    @Override
    @AddSentinel(exception = SentinelExceptionEnum.NO_PROVIDER)
    public String test3(String name) {
        return "no_provider";
    }

}
