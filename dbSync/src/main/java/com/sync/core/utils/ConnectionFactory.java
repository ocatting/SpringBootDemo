package com.sync.core.utils;

import java.sql.Connection;

/**
 * @Description: 连接工厂
 * @Author: Yan XinYu
 * @Date: 2021-03-04 11:02
 */
public interface ConnectionFactory {

    /**
     * 获取连接
     * @return 连接
     */
    Connection getConnecttion();

    /**
     * 获取新的连接
     * @return 连接
     */
    Connection getConnecttionWithoutRetry();

    /**
     * 获取连接信息
     * @return 信息
     */
    String getConnectionInfo();

}
