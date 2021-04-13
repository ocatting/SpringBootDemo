package com.sync.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: refer:http://blog.csdn.net/ring0hx/article/details/6152528
 * @Author: Yan XinYu
 * @Date: 2021-03-04 11:02
 */
public enum DataBaseType {
    /**
     * 数据库驱动类型
     */
    MySql("mysql", "com.mysql.cj.jdbc.Driver"),
    Oracle("oracle", "oracle.jdbc.OracleDriver"),
    SQLServer("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    PostgreSQL("postgresql", "org.postgresql.Driver"),

    ;

    private String typeName;
    private String driverClassName;

    DataBaseType(String typeName, String driverClassName) {
        this.typeName = typeName;
        this.driverClassName = driverClassName;
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }


    public String formatPk(String splitPk) {
        String result = splitPk;

        switch (this) {
            case MySql:
            case Oracle:
                if (splitPk.length() >= 2 && splitPk.startsWith("`") && splitPk.endsWith("`")) {
                    result = splitPk.substring(1, splitPk.length() - 1).toLowerCase();
                }
                break;
            case SQLServer:
                if (splitPk.length() >= 2 && splitPk.startsWith("[") && splitPk.endsWith("]")) {
                    result = splitPk.substring(1, splitPk.length() - 1).toLowerCase();
                }
                break;
            case PostgreSQL:
            	break;
            default:
                throw new RuntimeException( "unsupported database type.");
        }

        return result;
    }


    public String quoteColumnName(String columnName) {
        String result = columnName;

        switch (this) {
            case MySql:
                result = "`" + columnName.replace("`", "``") + "`";
                break;
            case Oracle:
                break;
            case SQLServer:
                result = "[" + columnName + "]";
                break;
            case PostgreSQL:
                break;
            default:
                throw new RuntimeException(  "unsupported database type");
        }

        return result;
    }

    public String quoteTableName(String tableName) {
        String result = tableName;

        switch (this) {
            case MySql:
                result = "`" + tableName.replace("`", "``") + "`";
                break;
            case Oracle:
            case SQLServer:
            case PostgreSQL:
                break;
            default:
                throw new RuntimeException( "unsupported database type");
        }

        return result;
    }

    private static Pattern mysqlPattern = Pattern.compile("jdbc:mysql://(.+):\\d+/.+");
    private static Pattern oraclePattern = Pattern.compile("jdbc:oracle:thin:@(.+):\\d+:.+");

    /**
     * 注意：目前只实现了从 mysql/oracle 中识别出ip 信息.未识别到则返回 null.
     */
    public static String parseIpFromJdbcUrl(String jdbcUrl) {
        Matcher mysql = mysqlPattern.matcher(jdbcUrl);
        if (mysql.matches()) {
            return mysql.group(1);
        }
        Matcher oracle = oraclePattern.matcher(jdbcUrl);
        if (oracle.matches()) {
            return oracle.group(1);
        }
        return null;
    }
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
