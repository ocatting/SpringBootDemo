package com.yu.dubbo.rmi;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @Description: RMI 注册中心
 * @Author Yan XinYu
 **/
public class RMIRegister {

    public static void main(String[] args) throws IOException {
        Registry registry = LocateRegistry.createRegistry(8080);
        System.in.read();
    }

}
