package com.yu.h2.demo;

import java.sql.*;

/**
 * @Description: H2 可以内嵌运行与 当做服务程序运行
 * @Author Yan XinYu
 **/
public class H2Demo {

    /**
     * 以嵌入式(本地)连接方式连接H2数据库
     */
//    private static final String JDBC_URL = "jdbc:h2:D:/AWork/IdeaWorkSpace/SpringBootDemo/spring-h2-demo/src/main/resources/temp/msg";
    private static final String JDBC_URL = "jdbc:h2:file:D:/AWork/h2/msg_file";

    /**
     * 使用TCP/IP的服务器模式(远程连接)方式连接H2数据库(推荐)
     */
    // private static final String JDBC_URL = "jdbc:h2:tcp://10.35.14.122/C:/H2/user";

    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER_CLASS = "org.h2.Driver";

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Class.forName(DRIVER_CLASS);
        Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        Statement statement = conn.createStatement();

        statement.execute("DROP TABLE IF EXISTS USER_INF");
        statement.execute("CREATE TABLE USER_INF(id INTEGER PRIMARY KEY, name VARCHAR(100), sex VARCHAR(2))");

        statement.executeUpdate("INSERT INTO USER_INF VALUES(1, 'tom', '男') ");
        statement.executeUpdate("INSERT INTO USER_INF VALUES(2, 'jack', '女') ");
        statement.executeUpdate("INSERT INTO USER_INF VALUES(3, 'marry', '男') ");
        statement.executeUpdate("INSERT INTO USER_INF VALUES(4, 'lucy', '男') ");

        ResultSet resultSet = statement.executeQuery("select * from USER_INF");

        while (resultSet.next()) {
            System.out.println(
                    resultSet.getInt("id") + ", " + resultSet.getString("name") + ", " + resultSet.getString("sex"));
        }

        statement.close();
        conn.close();
    }
}
