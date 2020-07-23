package com.yu.dubbo;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Description: 服务的消费者 原生开发
 * @Author Yan XinYu
 **/
public class DubboDemoClient {

    public void start()throws IOException {

        ApplicationConfig applicationConfig = new ApplicationConfig("dubbo-demo-client");

        RegistryConfig registryConfig = new RegistryConfig("multicast://224.1.1.1:3333");

        ReferenceConfig referenceConfig = new ReferenceConfig();
        referenceConfig.setInterface(DubboTestService.class);
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);

        DubboTestService dubboTestService = (DubboTestService) referenceConfig.get();
        System.out.println(dubboTestService.getName());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while (true){
            String s = bufferedReader.readLine();
            if(s.equals("quit")){
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        DubboDemoClient dubboDemoClient = new DubboDemoClient();
        dubboDemoClient.start();
    }

}
