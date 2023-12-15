package com.whl.spring.cloud.demo.provider.rpc.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.whl.spring.cloud.demo.service.StorageService;

import io.seata.core.context.RootContext;

@DubboService
public class StorageServiceImpl implements StorageService {
    private static final String SUCCESS = "SUCCESS";

    private static final String FAIL = "FAIL";

    private static Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String storage(String commodityCode, int count) {
        logger.info("Storage Service Begin ... xid: " + RootContext.getXID());
        int result = this.jdbcTemplate.update(
                "update storage_tbl set count = count - ? where commodity_code = ?",
                new Object[] { count, commodityCode });
        logger.info("Storage Service End ... ");
        if (result == 1) {
            return SUCCESS;
        }
        return FAIL;
    }

}
