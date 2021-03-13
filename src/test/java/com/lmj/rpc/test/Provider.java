package com.lmj.rpc.test;

import com.lmj.rpc.registry.ServerInfo;
import com.lmj.rpc.registry.ZookeeperRegistry;
import com.lmj.rpc.transport.BeanManager;
import com.lmj.rpc.transport.RpcServer;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * Created on 2020-06-21
 */
public class Provider {
    public static void main(String[] args) throws Exception {
        // 创建DemoServiceImpl，并注册到BeanManager中
        BeanManager.registerBean(DemoService.class.getSimpleName(), new DemoServiceImpl());
        // 创建ZookeeperRegistry，并将Provider的地址信息封装成ServerInfo
        // 对象注册到Zookeeper
        ZookeeperRegistry<ServerInfo> discovery = new ZookeeperRegistry<>();
        discovery.start();
        ServerInfo serverInfo = new ServerInfo("127.0.0.1", 20880);
        discovery.registerService(
                ServiceInstance.<ServerInfo>builder().name("DemoService").payload(serverInfo).build());
        // 启动DemoRpcServer，等待Client的请求
        RpcServer rpcServer = new RpcServer(20880);
        rpcServer.start();
        Thread.sleep(100000000L);
    }
}