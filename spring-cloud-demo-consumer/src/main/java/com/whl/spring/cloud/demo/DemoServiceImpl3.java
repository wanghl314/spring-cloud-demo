package com.whl.spring.cloud.demo;

import com.whl.spring.cloud.demo.sentinel.annotation.AddSentinel;
import com.whl.spring.cloud.demo.sentinel.annotation.DemoSentinelResource;
import com.whl.spring.cloud.demo.sentinel.SentinelExceptionEnum;
import com.whl.spring.cloud.demo.service.DemoServiceImpl;

@DemoSentinelResource
public class DemoServiceImpl3 extends DemoServiceImpl {

    @Override
    @AddSentinel(exception = SentinelExceptionEnum.NO_PROVIDER)
    public String test3(String name) {
        return "no_provider";
    }

}
