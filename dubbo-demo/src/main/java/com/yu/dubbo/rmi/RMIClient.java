package com.yu.dubbo.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @Description: 获取 服务端 service
 * java 自带远程调用实现
 * 角色: 服务端(提供者) ， 客户端(消费者) ，RMI注册中心
 *
 * 该客户端会直接从RMI注册中心获取到实体类，
 * 所以实体类尽量实例化，且实体类不能有依赖。
 * @Author Yan XinYu
 **/
public class RMIClient {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {

        UserService userService = (UserService) Naming.lookup("rmi://localhost:8080/userService");
        System.out.println("远程调用:"+userService.getName());

    }
}
