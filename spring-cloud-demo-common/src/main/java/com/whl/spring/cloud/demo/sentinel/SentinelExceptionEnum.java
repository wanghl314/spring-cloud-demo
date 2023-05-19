package com.whl.spring.cloud.demo.sentinel;

public enum SentinelExceptionEnum {
    ALL,

    /**
     * no provider
     */
    NO_PROVIDER,

    /**
     * 网络超时相关
     */
    NETWORK
}
