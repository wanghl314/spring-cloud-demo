package com.whl.spring.cloud.demo.dubbo;

import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

public interface DubboFallback {
    Result handle(Invoker<?> invoker, Invocation invocation, RpcException e);
}
