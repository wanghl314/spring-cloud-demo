package com.whl.spring.cloud.demo.dubbo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.RpcInvocation;
import org.apache.dubbo.rpc.support.RpcUtils;
import org.springframework.util.CollectionUtils;

import com.whl.spring.cloud.demo.annotation.AddSentinel;
import com.whl.spring.cloud.demo.annotation.SentinelResource;
import com.whl.spring.cloud.demo.sentinel.SentinelExceptionEnum;
import com.whl.spring.cloud.demo.sentinel.SentinelResult;
import com.whl.spring.cloud.demo.util.ReflectionUtils;

public class DefaultDubboFallback implements DubboFallback {

    @Override
    public Result handle(Invoker<?> invoker, Invocation invocation, RpcException e) {
        try {
            SentinelResult sentinelResult = null;

            if (DubboUtils.isNetWorkException(e)) {
                sentinelResult = this.match(invocation, SentinelExceptionEnum.NETWORK);
            } else if (DubboUtils.isNoProviderException(e)) {
                sentinelResult = this.match(invocation, SentinelExceptionEnum.NO_PROVIDER);
            }

            if (sentinelResult != null) {
                Object result = ReflectionUtils.invoke(sentinelResult.getMethod(), sentinelResult.getArgs(), sentinelResult.getService());
                return AsyncRpcResult.newDefaultAsyncResult(result, invocation);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return AsyncRpcResult.newDefaultAsyncResult(e, invocation);
    }

    private SentinelResult match(Invocation invocation, SentinelExceptionEnum se) throws ClassNotFoundException, NoSuchMethodException {
        List<SentinelResult> resultList = null;
        RpcInvocation rpcInvocation = (RpcInvocation) invocation;
        Set<Class<?>> serviceImpl = ReflectionUtils.getSubTypesOf(rpcInvocation.getServiceName());

        if (!CollectionUtils.isEmpty(serviceImpl)) {
            resultList = new ArrayList<SentinelResult>();

            for (Class<?> service : serviceImpl) {
                SentinelResource sentinelResource = service.getAnnotation(SentinelResource.class);

                if (sentinelResource != null) {
                    Method method = service.getMethod(RpcUtils.getMethodName(invocation), RpcUtils.getParameterTypes(invocation));
                    AddSentinel addSentinel = method.getAnnotation(AddSentinel.class);

                    if (addSentinel == null) {
                        addSentinel = service.getAnnotation(AddSentinel.class);
                    }

                    if (addSentinel != null) {
                        boolean match = checkSentinelException(se, addSentinel.exception());

                        if (match) {
                            SentinelResult sentinelResult = new SentinelResult(service, method, RpcUtils.getArguments(invocation));
                            resultList.add(sentinelResult);
                        }
                    }
                }
            }

            if (!CollectionUtils.isEmpty(resultList)) {
                return resultList.get(0);
            }
        }
        return null;
    }

    private boolean checkSentinelException(SentinelExceptionEnum source, SentinelExceptionEnum[] target) {
        Set<SentinelExceptionEnum> list = new HashSet<SentinelExceptionEnum>(Arrays.asList(target));

        if (list.contains(SentinelExceptionEnum.ALL)) {
            return true;
        }
        return list.contains(source);
    }

}
