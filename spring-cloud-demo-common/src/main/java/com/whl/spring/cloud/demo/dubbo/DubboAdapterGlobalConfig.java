package com.whl.spring.cloud.demo.dubbo;


public class DubboAdapterGlobalConfig {
    private static volatile DubboFallback consumerFallback = new DefaultDubboFallback();
    private static volatile DubboFallback providerFallback = new DefaultDubboFallback();

    public static DubboFallback getConsumerFallback() {
        return consumerFallback;
    }

    public static DubboFallback getProviderFallback() {
        return providerFallback;
    }

}
