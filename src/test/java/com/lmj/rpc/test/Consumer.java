package com.lmj.rpc.test;

import com.lmj.rpc.proxy.RpcProxy;
import com.lmj.rpc.registry.ServerInfo;
import com.lmj.rpc.registry.ZookeeperRegistry;

public class Consumer {
    public static void main(String[] args) throws Exception {
        // 创建ZookeeperRegistr对象
        ZookeeperRegistry<ServerInfo> discovery = new ZookeeperRegistry<>();
        discovery.start();

        // 创建代理对象，通过代理调用远端Server
        DemoService demoService = RpcProxy.newInstance(DemoService.class, discovery);
        // 调用sayHello()方法，并输出结果
        String result = demoService.sayHello("hello");
        System.out.println(result);
        Thread.sleep(1000000L);
    }
}