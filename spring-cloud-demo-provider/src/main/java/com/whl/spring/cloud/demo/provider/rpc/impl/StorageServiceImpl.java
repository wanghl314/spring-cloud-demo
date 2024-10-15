package com.whl.spring.cloud.demo.provider.rpc.impl;

import com.whl.spring.cloud.demo.service.StorageService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

@DubboService
public class StorageServiceImpl implements StorageService {
    private static final String SUCCESS = "SUCCESS";

    private static final String FAIL = "FAIL";

    private static Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Autowired
    private JdbcClient jdbcClient;

    @Override
    public String storage(String commodityCode, int count) {
        logger.info("Storage Service Begin ... xid: " + RootContext.getXID());
        int result = this.jdbcClient.sql("update storage_tbl set count = count - ? where commodity_code = ?")
                .params(count, commodityCode)
                .update();
        logger.info("Storage Service End ... ");
        if (result == 1) {
            return SUCCESS;
        }
        return FAIL;
    }

}
