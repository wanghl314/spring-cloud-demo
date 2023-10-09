package com.whl.spring.cloud.demo.consumer.service.impl;

import java.sql.PreparedStatement;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.whl.spring.cloud.demo.consumer.bean.Order;
import com.whl.spring.cloud.demo.consumer.service.OrderService;

import io.seata.core.context.RootContext;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String SUCCESS = "SUCCESS";

    private static final String FAIL = "FAIL";

    private static final String USER_ID = "U100001";

    private static final String COMMODITY_CODE = "C00321";

    private static final String SERVICE_NAME = "spring-cloud-demo-provider";

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RestTemplate restTemplate;

    private Random random = new Random();

    @Override
    public String order(String userId, String commodityCode, int orderCount) {
        logger.info("Order Service Begin ... xid: " + RootContext.getXID());

        int orderMoney = this.calculate(commodityCode, orderCount);

        this.invokerAccountService(orderMoney);

        final Order order = new Order();
        order.userId = userId;
        order.commodityCode = commodityCode;
        order.count = orderCount;
        order.money = orderMoney;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = jdbcTemplate.update(conn -> {
            PreparedStatement pst = conn.prepareStatement(
                    "insert into order_tbl (user_id, commodity_code, count, money) values (?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setObject(1, order.userId);
            pst.setObject(2, order.commodityCode);
            pst.setObject(3, order.count);
            pst.setObject(4, order.money);
            return pst;
        }, keyHolder);

        order.id = keyHolder.getKey().longValue();

        if (random.nextBoolean()) {
            throw new RuntimeException("this is a mock Exception");
        }

        logger.info("Order Service End ... Created " + order);

        if (result == 1) {
            return SUCCESS;
        }
        return FAIL;
    }

    private int calculate(String commodityId, int orderCount) {
        return 2 * orderCount;
    }

    private void invokerAccountService(int orderMoney) {
        String url = String.format("http://%s/account", SERVICE_NAME);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        map.add("userId", USER_ID);
        map.add("money", orderMoney + "");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
                map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request,
                String.class);
    }

}
