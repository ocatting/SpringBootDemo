package com.yu.dubbo.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class UserServiceImpl extends UnicastRemoteObject implements UserService {

    protected UserServiceImpl() throws RemoteException {

    }

    @Override
    public String getName() {
        return "yanxinyu";
    }
}
