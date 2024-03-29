package com.whl.spring.cloud.demo.provider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/demo")
public class DemoController {
    private static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @SentinelResource(value = "demoController#index", blockHandler = "handlerIndex")
    @GetMapping(value = {"", "/"})
    public String index() throws Exception {
        logger.info("provider /demo");
        return "Demo";
    }

    @SentinelResource(value = "demoController#test", blockHandler = "handlerTest")
    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("provider /demo/test");
        response.setHeader("test", "test");
        return request.getServletContext().getRealPath("");
    }

    @GetMapping("/testRestTemplateDegrade")
    public String testRestTemplateDegrade(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("provider /demo/testRestTemplateDegrade");
        throw new RuntimeException("固定返回异常信息");
    }

    public String handlerIndex(BlockException e) {
        return "/demo: 请求过于频繁，请稍后再试。";
    }

    public String handlerTest(HttpServletRequest request, HttpServletResponse response, BlockException e) {
        return "/demo/test: 请求过于频繁，请稍后再试。";
    }

}
