package com.whl.spring.cloud.demo.util;

import org.apache.dubbo.rpc.RpcException;

public class DubboUtils {

    public static boolean isNetWorkException(RpcException e) {
        if (e.isTimeout() || e.isNetwork()) {
            return true;
        }
        return false;
    }

    public static boolean isNoProviderException(RpcException e) {
        if (e.isForbidden() || e.isNoInvokerAvailableAfterFilter()) {
            return true;
        } else {
            String message = e.getMessage();

            if (message.contains("Service [") && message.contains("] not found.")) {
                return true;
            }
        }
        return false;
    }

}
