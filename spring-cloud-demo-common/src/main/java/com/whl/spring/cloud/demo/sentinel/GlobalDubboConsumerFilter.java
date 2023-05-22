package com.whl.spring.cloud.demo.sentinel;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.filter.ClusterFilter;

import com.whl.spring.cloud.demo.sentinel.config.DubboAdapterGlobalConfig;

@Activate(group = CommonConstants.CONSUMER)
public class GlobalDubboConsumerFilter implements ClusterFilter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            return invoker.invoke(invocation);
        } catch (RpcException e) {
            return DubboAdapterGlobalConfig.getConsumerFallback().handle(invoker, invocation, e);
        }
    }

}
