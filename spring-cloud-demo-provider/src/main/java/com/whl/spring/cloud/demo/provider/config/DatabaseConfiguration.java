package com.whl.spring.cloud.demo.provider.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.annotation.PostConstruct;

@Configuration
public class DatabaseConfiguration {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        this.jdbcTemplate.update("delete from storage_tbl where commodity_code = 'C00321'");
        this.jdbcTemplate.update(
                "insert into storage_tbl(commodity_code, count) values ('C00321', 100)");

        this.jdbcTemplate.update("delete from account_tbl where user_id = 'U100001'");
        this.jdbcTemplate.update(
                "insert into account_tbl(user_id, money) values ('U100001', 10000)");
    }

}
