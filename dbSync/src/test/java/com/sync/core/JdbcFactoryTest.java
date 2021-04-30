package com.sync.core;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.sync.core.utils.DBUtil;
import com.sync.core.utils.ListTriple;
import com.sync.entity.SyncDb;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-04-13 14:50
 */
@Slf4j
public class JdbcFactoryTest {

    /**
     * HIVE连接：
     * 测试环境：192.168.200.61:10000 ，用户名：hive，密码：nNOOwtwEY0TEclkt
     * 生产环境：100.98.165.27:10006 ，用户名：hive，密码：dZcnvxo4UlCQVBa1
     */
    @Test
    public void hiveTest() throws SQLException {
        System.out.println("hive test...");

        SyncDb syncDb = new SyncDb();
        syncDb.setId(1);
        syncDb.setName("测试");
        syncDb.setDriver("com.mysql.cj.jdbc.Driver");
        syncDb.setUsername("partol_cloud");
        syncDb.setPassword("cloud_partol");
        syncDb.setJdbcUrl("jdbc:mysql://100.98.165.105:3306/partol_cloud?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai");

//        SyncDb syncDb = new SyncDb();
//        syncDb.setId(1);
//        syncDb.setName("hive测试");
//        syncDb.setDriver("org.apache.hive.jdbc.HiveDriver");
//        syncDb.setUsername("hive");
//        syncDb.setPassword("nNOOwtwEY0TEclkt");
//        syncDb.setJdbcUrl("jdbc:hive2://192.168.200.61:10000/ods_dflow");
//        syncDb.setDbType("hive2");

        System.out.println("数据库:"+syncDb.getDatabase());
        Connection connection = JdbcFactory.getConnection(syncDb);
//        List<String> list = DBUtil.getTables(connection, syncDb.getDatabase());
//        String tableName = "car_snapshot_record";
//        ListTriple columnMetaData = DBUtil.getColumnMetaData(connection, tableName,"*");
//        System.out.println(columnMetaData);

        String sql = "SELECT a.*,b.mac as device_id FROM patrol_strategy_daily_item a LEFT JOIN patrol_gateway b on a.gateway_id = b.id  WHERE 1=2";
        ResultSet query = DBUtil.query(connection, sql);
        ResultSetMetaData metaData = query.getMetaData();
        int columnNumber = metaData.getColumnCount();
        ResultSetMetaData metaData1 = query.getMetaData();
        for (int i = 1; i <= columnNumber; i++) {
            String columnName = metaData1.getColumnLabel(i);
            System.out.println("columnName:"+columnName);
        }
        while (query.next()){
            Map<String,String> map = new HashMap<>();
            for (int i = 1; i <= columnNumber; i++) {
                String columnName = metaData1.getColumnName(i);
                map.put(columnName,query.getString(i));
            }
            System.out.println("line:"+map);
        }
    }

    public static void main(String[] args) throws SQLException {
        System.out.println("测试");
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123456");
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        druidDataSource.setDbType("mysql");
        druidDataSource.setMaxActive(2);
        druidDataSource.setInitialSize(1);
        druidDataSource.setMaxWait(5000);
        druidDataSource.setMinIdle(0);

        // 原来池子里的东西使用完成后还是要还回去的。connection1.close()
        DruidPooledConnection connection1 = druidDataSource.getConnection();
        System.out.println("connection1:"+connection1.isValid(2000));
        DruidPooledConnection connection2 = druidDataSource.getConnection();
        System.out.println("connection2:"+connection2.isValid(2000));
        DruidPooledConnection connection3 = druidDataSource.getConnection();
        System.out.println("connection3:"+connection3.isValid(2000));
    }

}