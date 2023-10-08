package com.whl.spring.cloud.demo.consumer.service;

public interface OrderService {
    String order(String userId, String commodityCode, int orderCount);
}
