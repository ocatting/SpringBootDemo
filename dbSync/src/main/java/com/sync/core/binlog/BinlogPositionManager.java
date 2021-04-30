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


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description: MySQL消费位置管理
 * @Author: Yan XinYu
 * @Date: 2021-04-25 22:40
 */
@Slf4j
public class BinlogPositionManager {

    private final Config config;

    private String binlogFilename;
    private Long nextPosition;

    public BinlogPositionManager(Config config) {
        this.config = config;
    }

    public void initBeginPosition() throws Exception {

        if (config.getStartType() == null || config.getStartType().equals("DEFAULT")) {
            initPositionDefault();

        } else if (config.getStartType().equals("NEW_EVENT")) {
            initPositionFromBinlogTail();

        } else if (config.getStartType().equals("LAST_PROCESSED")) {
//            initPositionFromMqTail();

        } else if (config.getStartType().equals("SPECIFIED")) {
            binlogFilename = config.getBinlogFilename();
            nextPosition = config.getNextPosition();
        }

        if (binlogFilename == null || nextPosition == null) {
            throw new Exception("binlogFilename | nextPosition is null.");
        }
    }

    private void initPositionDefault() throws Exception {

//        try {
//            initPositionFromMqTail();
//        } catch (Exception e) {
//            logger.error("Init position from mq error.", e);
//        }

        if (binlogFilename == null || nextPosition == null) {
            initPositionFromBinlogTail();
        }

    }

    private void initPositionFromBinlogTail() throws Exception {
        String sql = "SHOW MASTER STATUS";

        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = config.getDataSource().getConnection();
            rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                binlogFilename = rs.getString("File");
                nextPosition = rs.getLong("Position");
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

    }

    public String getBinlogFilename() {
        return binlogFilename;
    }

    public Long getPosition() {
        return nextPosition;
    }
}
