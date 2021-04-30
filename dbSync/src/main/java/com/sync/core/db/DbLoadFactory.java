package com.sync.core.db;

import com.sync.core.constant.CommonConstant;
import com.sync.core.db.mysql.MysqlReader;
import com.sync.core.db.mysql.MysqlWriter;
import com.sync.core.db.postgres.PostgresReader;
import com.sync.core.db.postgres.PostgresWriter;
import com.sync.core.db.rdb.RdbReader;

/**
 * @Description: db工厂
 * @Author: Yan XinYu
 * @Date: 2021-03-03 22:26
 */

public class DbLoadFactory {

    public static Reader getReader(String dbType){
        if(CommonConstant.DB_TYPE_MYSQL.equalsIgnoreCase(dbType)){
            return new MysqlReader();
        } else if (CommonConstant.DB_TYPE_PGSQL.equalsIgnoreCase(dbType)){
            return new PostgresReader();
        } else {
            return new RdbReader();
        }
    }

    public static Writer getWriter(String dbType){
        if(CommonConstant.DB_TYPE_MYSQL.equalsIgnoreCase(dbType)){
            return new MysqlWriter();
        } else if (CommonConstant.DB_TYPE_PGSQL.equalsIgnoreCase(dbType)){
            return new PostgresWriter();
        } else {
            throw new RuntimeException("未匹配的数据类型");
        }
    }

}
