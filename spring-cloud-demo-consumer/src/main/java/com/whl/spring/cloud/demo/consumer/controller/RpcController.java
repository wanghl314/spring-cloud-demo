package com.whl.spring.cloud.demo.consumer.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.whl.spring.cloud.demo.DemoService;
import com.whl.spring.cloud.demo.UserService;

@RestController
@RequestMapping("/rpc")
public class RpcController {
    @DubboReference
    private UserService userService;

    @DubboReference(group = "test")
    private DemoService demoService;

    @GetMapping("/user")
    public String sayHello(String name) throws Exception {
        this.userService.sayHello(name);
        return "SUCCESS";
    }

    @GetMapping("/demo")
    public String test(String name) throws Exception {
        return this.demoService.test(name);
    }

    @GetMapping("/demo2")
    public String test2(String name) throws Exception {
        return this.demoService.test2(name);
    }

    @GetMapping("/demo3")
    public String test3(String name) throws Exception {
        return this.demoService.test3(name);
    }

    @GetMapping("/testDegrade")
    public String testDegrade(String name) throws Exception {
        try {
            return this.demoService.testDegrade(name);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    private String handleException(Exception e) throws Exception {
        boolean degrade = false;
        Throwable t = e;

        do {
            if (t instanceof DegradeException) {
                degrade = true;
                break;
            }
            t = t.getCause();
        } while (t != null);

        if (!degrade && e instanceof RuntimeException &&
                (BlockException.BLOCK_EXCEPTION_MSG_PREFIX + DegradeException.class.getSimpleName()).equals(e.getMessage())) {
            degrade = true;
        }

        if (degrade) {
            return "Dubbo: 触发降级保护";
        }
        throw e;
    }

}
