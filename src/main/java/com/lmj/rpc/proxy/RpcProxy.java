package com.lmj.rpc.proxy;

import com.lmj.rpc.Constants;
import com.lmj.rpc.protocol.Header;
import com.lmj.rpc.protocol.Message;
import com.lmj.rpc.protocol.Request;
import com.lmj.rpc.registry.Registry;
import com.lmj.rpc.registry.ServerInfo;
import com.lmj.rpc.transport.Connection;
import com.lmj.rpc.transport.NettyResponseFuture;
import com.lmj.rpc.transport.RpcClient;
import io.netty.channel.ChannelFuture;
import org.apache.curator.x.discovery.ServiceInstance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.lmj.rpc.Constants.MAGIC;
import static com.lmj.rpc.Constants.VERSION;

public class RpcProxy implements InvocationHandler {

    private String serviceName; // 需要代理的服务(接口)名称

    public Map<Method, Header> headerCache = new ConcurrentHashMap<>();

    // 用于与Zookeeper交互，其中自带缓存
    private Registry<ServerInfo> registry;

    public RpcProxy(String serviceName,
                    Registry<ServerInfo> registry) throws Exception {
        this.serviceName = serviceName;
        this.registry = registry;
    }

    public static <T> T newInstance(Class<T> clazz, Registry<ServerInfo> registry) throws Exception {
        // 创建代理对象
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{clazz},
                new RpcProxy(clazz.getSimpleName(), registry));
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 从Zookeeper缓存中获取可用的Server地址,并随机从中选择一个
        List<ServiceInstance<ServerInfo>> serviceInstances =
                registry.queryForInstances(serviceName);
        ServiceInstance<ServerInfo> serviceInstance =
                serviceInstances.get(ThreadLocalRandom.current().nextInt(serviceInstances.size()));
        // 创建请求消息，然后调用remoteCall()方法请求上面选定的Server端
        String methodName = method.getName();
        Header header = headerCache.computeIfAbsent(method, h -> new Header(MAGIC, VERSION));
        Message<Request> message = new Message<>(header, new Request(serviceName, methodName, args));
        return remoteCall(serviceInstance.getPayload(), message);
    }

    protected Object remoteCall(ServerInfo serverInfo, Message message) throws Exception {
        if (serverInfo == null) {
            throw new RuntimeException("get available server error");
        }
        Object result;
        try {
            // 创建DemoRpcClient连接指定的Server端
            RpcClient demoRpcClient = new RpcClient(serverInfo.getHost(), serverInfo.getPort());
            ChannelFuture channelFuture = demoRpcClient.connect().awaitUninterruptibly();
            // 创建对应的Connection对象，并发送请求
            Connection connection = new Connection(channelFuture, true);
            NettyResponseFuture responseFuture = connection.request(message, Constants.DEFAULT_TIMEOUT);
            // 等待请求对应的响应
            result = responseFuture.getPromise().get(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw e;
        }
        return result;
    }
}