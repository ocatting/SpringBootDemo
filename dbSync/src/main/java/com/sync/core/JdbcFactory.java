package com.sync.core;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.sync.entity.SyncDb;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 数据库连接问题
 * @Author: Yan XinYu
 * @Date: 2021-03-11 12:35
 */

public class JdbcFactory {

    /**
     * 注册数据库配置信息
     */
    private static final Map<Integer, SyncDb> REGISTER_DB = new ConcurrentHashMap<>(16);

    /**
     * 数据库连接池
     */
    private static final Map<Integer, DruidDataSource> DB_CONNECT = new ConcurrentHashMap<>(16);

    /**
     * 外部的连接池，给远程调用使用
     */
    private static final Map<String,DruidDataSource> EXTERNAL_DB_CONNECT = new ConcurrentHashMap<>(16);

    public static void setRegisterDbAll(List<SyncDb> syncDbs){
        for (SyncDb syncDb : syncDbs) {
            REGISTER_DB.put(syncDb.getId(),syncDb);
        }
    }

    public static void shutdown(){
        for (DruidDataSource db: DB_CONNECT.values()) {
            db.close();
        }
        for (DruidDataSource db: EXTERNAL_DB_CONNECT.values()){
            db.close();
        }
    }

    public static SyncDb get(Integer id){
        return REGISTER_DB.get(id);
    }

    public static Connection getConnection(SyncDb syncDb){
        if(syncDb == null){
            throw new RuntimeException("数据库参数错误:syncDb is null ");
        }
        String key = JSON.toJSONString(syncDb);
        try {
            if (EXTERNAL_DB_CONNECT.containsKey(key)) {
               return EXTERNAL_DB_CONNECT.get(key).getConnection();
            }
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUrl(syncDb.getJdbcUrl());
            druidDataSource.setUsername(syncDb.getUsername());
            druidDataSource.setPassword(syncDb.getPassword());
            druidDataSource.setDriverClassName(syncDb.getDriver());
            druidDataSource.setDbType(syncDb.getDbType());
            druidDataSource.setMaxActive(100);
            druidDataSource.setInitialSize(1);
            druidDataSource.setMaxWait(60000);
            druidDataSource.setMinIdle(1);
            EXTERNAL_DB_CONNECT.put(JSON.toJSONString(syncDb),druidDataSource);
            return druidDataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("数据库连接异常:dbId:"+syncDb.getId()+",name:"+syncDb.getName()+",JDBC:"+syncDb.getJdbcUrl(),e);
        }
    }

}
