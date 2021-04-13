package com.sync.core.utils;

import com.sync.core.JdbcFactory;
import com.sync.entity.SyncDb;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * @Description: 读工具
 * @Author: Yan XinYu
 * @Date: 2021-03-04 16:15
 */
@Slf4j
public class ReaderUtil {

    public static String readIncrementValByOne(SyncDb syncDb, String querySql){
        Connection writeConn = JdbcFactory.getConnection(syncDb);
        Statement stmt = null;
        Object value = null;
        try {
            stmt = writeConn.createStatement();
            stmt.setQueryTimeout(5000);

            ResultSet resultSet = stmt.executeQuery(querySql);
            if (resultSet.next()){
                value = DBUtil.getResultSetValue(resultSet,1, String.class);
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error("increment sql 配置异常",e);
        } finally {
            DBUtil.closeDBResources(stmt,writeConn);
        }
        return Objects.isNull(value)?null:value.toString();
    }


}
