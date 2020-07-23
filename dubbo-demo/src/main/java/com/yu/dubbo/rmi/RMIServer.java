package com.yu.dubbo.rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * @Description: RMI 服务端(提供者)
 * @Author Yan XinYu
 **/
public class RMIServer {

    public static void main(String[] args) throws IOException, AlreadyBoundException {
        UserService userService = new UserServiceImpl();

        Naming.bind("rmi://localhost:8080/userService",userService);
        System.out.println("远程提供服务 getName :");

        System.in.read();
    }
}
