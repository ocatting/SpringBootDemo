package com.yu.dubbo;

import org.apache.dubbo.config.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: dubbo服务提供者 原生开发
 *
 * ApplicationConfig #服务应用名称
 * ProtocolConfig    #服务协议配置
 * RegistryConfig    #注册配置地址
 * ServiceConfig     #服务接口注册并暴露
 *
 * @Author Yan XinYu
 **/
public class DubboDemoServer {

    public static void main(String[] args) throws IOException {
        DubboDemoServer dubboDemoServer = new DubboDemoServer();
        dubboDemoServer.start();

        System.out.println("服务已暴露");
        System.in.read();
    }

    public void start (){
        ApplicationConfig applicationConfig = new ApplicationConfig("dubbo-server-demo");

        ProtocolConfig protocolConfig = new ProtocolConfig("dubbo",-1);

        /**
         * multicast://224.1.1.1:3333 组网广播，适用于局域网
         * zookeeper://127.0.0.1:2181
         * redis://180.76.245.64:9393
         */
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");

        ServiceConfig serviceConfig = new ServiceConfig();
        serviceConfig.setInterface(DubboTestService.class);
        serviceConfig.setRef(new DubboTestServiceImpl());
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setProtocol(protocolConfig);
        serviceConfig.setApplication(applicationConfig);

        serviceConfig.export();
    }

    /**
     * 负载均衡配置，默认负载为随机
     * @param serviceConfig
     */
    public void setLoadbalance(ServiceConfig serviceConfig){
        serviceConfig.setLoadbalance("");
    }
}
