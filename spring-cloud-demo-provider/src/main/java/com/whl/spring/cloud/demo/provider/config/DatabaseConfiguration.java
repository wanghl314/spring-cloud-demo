package com.whl.spring.cloud.demo.provider.config;

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
        this.jdbcClient.sql("delete from storage_tbl where commodity_code = 'C00321'").update();
        this.jdbcClient.sql("insert into storage_tbl(commodity_code, count) values ('C00321', 100)").update();

        this.jdbcClient.sql("delete from account_tbl where user_id = 'U100001'").update();
        this.jdbcClient.sql("insert into account_tbl(user_id, money) values ('U100001', 10000)").update();
    }

}
