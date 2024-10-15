package com.whl.spring.cloud.demo.consumer.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class DatabaseConfiguration {
    @Autowired
    private JdbcClient jdbcClient;

    @PostConstruct
    public void init() {
        this.jdbcClient.sql("TRUNCATE TABLE order_tbl").update();
    }

}
