package com.whl.spring.cloud.demo.provider.config;

import org.apache.dubbo.config.ProtocolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HessianConfig {

    @Bean("hessian")
    public ProtocolConfig hessian() {
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setId("hessian");
        protocol.setName("hessian");
        protocol.setServer("jetty");
        protocol.setHost("0.0.0.0");
        protocol.setPort(8090);
        protocol.setAccepts(500);
        protocol.setThreads(100);
        return protocol;
    }

}
