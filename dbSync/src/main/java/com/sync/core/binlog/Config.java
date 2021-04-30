/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sync.core.binlog;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.Data;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: MySQL复制配置文件
 * @Author: Yan XinYu
 * @Date: 2021-04-25 22:40
 */
@Data
public class Config implements java.io.Serializable {

    private String mysqlAddr;
    private Integer mysqlPort;
    private String mysqlUsername;
    private String mysqlPassword;

    private String startType = "DEFAULT";
    private String binlogFilename;
    private Long nextPosition;
    private Integer maxTransactionRows = 100;

    private DataSource dataSource;

    public DataSource getDataSource(){
        if( dataSource == null ){
            try {
                return initDataSource();
            } catch (Exception e) {
                throw new RuntimeException("数据库连接异常",e);
            }
        }
        return dataSource;
    }

    private DataSource initDataSource() throws Exception {
        Map<String, String> map = new HashMap<>(11);
        map.put("driverClassName", "com.mysql.jdbc.Driver");
        map.put("url", "jdbc:mysql://" + mysqlAddr + ":" + mysqlPort + "?useSSL=true&verifyServerCertificate=false");
        map.put("username", mysqlUsername);
        map.put("password", mysqlPassword);
        map.put("initialSize", "2");
        map.put("maxActive", "2");
        map.put("maxWait", "60000");
        map.put("timeBetweenEvictionRunsMillis", "60000");
        map.put("minEvictableIdleTimeMillis", "300000");
        map.put("validationQuery", "SELECT 1 FROM DUAL");
        map.put("testWhileIdle", "true");

        this.dataSource = DruidDataSourceFactory.createDataSource(map);
        return this.dataSource;
    }
}