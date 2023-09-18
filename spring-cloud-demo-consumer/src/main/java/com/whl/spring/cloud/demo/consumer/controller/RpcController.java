package com.whl.spring.cloud.demo.consumer.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(RpcController.class);

    @DubboReference
    private UserService userService;

    @DubboReference(group = "test")
    private DemoService demoService;

    @GetMapping("/user")
    public String sayHello(String name) throws Exception {
        logger.info("sayHello, {}", name);
        this.userService.sayHello(name);
        return "SUCCESS";
    }

    @GetMapping("/demo")
    public String test(String name) throws Exception {
        logger.info("test, {}", name);
        return this.demoService.test(name);
    }

    @GetMapping("/demo2")
    public String test2(String name) throws Exception {
        logger.info("test2, {}", name);
        return this.demoService.test2(name);
    }

    @GetMapping("/demo3")
    public String test3(String name) throws Exception {
        logger.info("test3, {}", name);
        return this.demoService.test3(name);
    }

    @GetMapping("/testDegrade")
    public String testDegrade(String name) throws Exception {
        logger.info("testDegrade, {}", name);

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
