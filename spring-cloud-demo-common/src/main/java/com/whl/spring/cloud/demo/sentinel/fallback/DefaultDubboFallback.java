package com.whl.spring.cloud.demo.sentinel.fallback;

import com.whl.spring.cloud.demo.sentinel.SentinelExceptionEnum;
import com.whl.spring.cloud.demo.sentinel.SentinelResult;
import com.whl.spring.cloud.demo.sentinel.annotation.AddSentinel;
import com.whl.spring.cloud.demo.sentinel.annotation.DemoSentinelResource;
import com.whl.spring.cloud.demo.util.DubboUtils;
import com.whl.spring.cloud.demo.util.ReflectionUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.support.RpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.*;

public class DefaultDubboFallback implements DubboFallback {
    private static Logger logger = LoggerFactory.getLogger(DefaultDubboFallback.class);

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
            logger.error(ex.getMessage(), ex);
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
                DemoSentinelResource demoResource = service.getAnnotation(DemoSentinelResource.class);

                if (demoResource != null) {
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
