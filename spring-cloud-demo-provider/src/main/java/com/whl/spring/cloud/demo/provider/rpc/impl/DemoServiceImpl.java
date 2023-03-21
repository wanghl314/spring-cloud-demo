package com.whl.spring.cloud.demo.provider.rpc.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.whl.spring.cloud.demo.DemoService;

@Service("demoServiceImpl")
public class DemoServiceImpl implements DemoService {

    @Override
    public String test(String name) {
        return "Hello " + name + ", this time is " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
