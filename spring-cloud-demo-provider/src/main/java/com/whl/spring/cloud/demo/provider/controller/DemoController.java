package com.whl.spring.cloud.demo.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
