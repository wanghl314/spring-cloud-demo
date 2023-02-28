package com.whl.spring.cloud.demo.provider.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping(value = {"", "/"})
    public String index() {
        return "Demo";
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("test", "test");
        return request.getServletContext().getRealPath("");
    }

}
