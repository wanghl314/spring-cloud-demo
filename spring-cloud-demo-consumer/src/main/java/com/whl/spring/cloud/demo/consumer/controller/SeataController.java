package com.whl.spring.cloud.demo.consumer.controller;

import com.whl.spring.cloud.demo.consumer.service.OrderService;
import com.whl.spring.cloud.demo.service.StorageService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seata")
public class SeataController {
    private static final String SUCCESS = "SUCCESS";

    private static final String FAIL = "FAIL";

    private static final String USER_ID = "U100001";

    private static final String COMMODITY_CODE = "C00321";

    private static final int ORDER_COUNT = 2;

    private static Logger logger = LoggerFactory.getLogger(SeataController.class);

    @Autowired
    private OrderService orderService;

    @DubboReference
    private StorageService storageService;

    @GlobalTransactional(timeoutMills = 300000, name = "spring-cloud-demo-tx")
    @GetMapping("/test")
    public String test() throws Exception {
        String result = this.storageService.storage(COMMODITY_CODE, ORDER_COUNT);

        if (!SUCCESS.equals(result)) {
            throw new RuntimeException();
        }
        result = this.orderService.order(USER_ID, COMMODITY_CODE, ORDER_COUNT);

        if (!SUCCESS.equals(result)) {
            throw new RuntimeException();
        }
        return SUCCESS;
    }

}
