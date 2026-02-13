package com.whl.spring.cloud.demo.provider.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping(value = {"", "/"})
    public String index() throws Exception {
        return "User";
    }

    @GetMapping("/test")
    public Object test() throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("a", RandomStringUtils.secure().nextAlphabetic(10));
        data.put("b", RandomStringUtils.secure().nextAlphanumeric(10));
        data.put("c", RandomStringUtils.secure().nextAscii(10));
        data.put("d", RandomStringUtils.secure().nextNumeric(10));
        return data;
    }

}
