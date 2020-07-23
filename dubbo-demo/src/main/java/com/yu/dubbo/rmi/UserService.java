package com.yu.dubbo.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface UserService extends Remote {

    String getName() throws RemoteException;
}
