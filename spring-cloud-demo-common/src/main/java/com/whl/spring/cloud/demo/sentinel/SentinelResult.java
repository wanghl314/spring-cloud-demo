package com.whl.spring.cloud.demo.sentinel;

import java.lang.reflect.Method;

public class SentinelResult {
    private Class<?> service;

    private Method method;

    private Object[] args;

    public SentinelResult() {
    }

    public SentinelResult(Class<?> service, Method method, Object[] args) {
        this.service = service;
        this.method = method;
        this.args = args;
    }

    public Class<?> getService() {
        return service;
    }

    public void setService(Class<?> service) {
        this.service = service;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
