package com.whl.spring.cloud.demo.provider.controller;

import org.apache.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/account")
public class AccountController {
    private static final String SUCCESS = "SUCCESS";

    private static final String FAIL = "FAIL";

    private static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Random random = new Random();

    @PostMapping(value = {"", "/"}, produces = "application/json")
    public String account(String userId, int money) {
        logger.info("Account Service ... xid: " + RootContext.getXID());

        if (random.nextBoolean()) {
            throw new RuntimeException("this is a mock Account Service Exception");
        }

        int result = jdbcTemplate.update(
                "update account_tbl set money = money - ? where user_id = ?",
                new Object[] { money, userId });
        logger.info("Account Service End ... ");
        if (result == 1) {
            return SUCCESS;
        }
        return FAIL;
    }

}
