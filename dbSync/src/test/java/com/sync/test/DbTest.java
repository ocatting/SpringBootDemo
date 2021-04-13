package com.sync.test;

import com.sync.core.JdbcFactory;
import com.sync.core.domain.SqlColumn;
import com.sync.core.domain.SqlIndex;
import com.sync.core.utils.DBUtil;
import com.sync.entity.SyncDb;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 数据库连接测试
 * @Author: Yan XinYu
 * @Date: 2021-03-11 9:24
 */
@Slf4j
public class DbTest {

    /**
     * 表存在判定测试
     * @throws SQLException
     */
    @Test
    public void existsTableTest() throws SQLException {

        SyncDb syncDb = new SyncDb();
        syncDb.setName("ddl");
        syncDb.setDbType("mysql");
        syncDb.setPassword("123456");
        syncDb.setUsername("root");
        syncDb.setJdbcUrl("jdbc:mysql://localhost:3306/bm?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&allowPublicKeyRetrieval=true");
        syncDb.setDriver("com.mysql.cj.jdbc.Driver");

        Connection connection = JdbcFactory.getConnection(syncDb);

        boolean existsTable = DBUtil.existsTable(connection, syncDb.getDatabase(),"t_user_copy");
        System.out.println("existsTable:"+existsTable);

        JdbcFactory.shutdown();
    }

    /**
     * 表ddl测试
     */
    @Test
    public void tableDdlTest() throws SQLException {
        log.info("start ...");

        SyncDb syncDb = new SyncDb();
        syncDb.setName("ddl");
        syncDb.setDbType("mysql");
        syncDb.setPassword("123456");
        syncDb.setUsername("root");
        syncDb.setJdbcUrl("jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&allowPublicKeyRetrieval=true");
        syncDb.setDriver("com.mysql.cj.jdbc.Driver");

        Connection connection = JdbcFactory.getConnection(syncDb);

        String ddl = DBUtil.getDdl(connection,"device_info");
        System.out.println("DDL:");
        System.out.println(ddl);

        JdbcFactory.shutdown();

        log.info("test complete!");
    }

    /**
     * 表结构同步测试
     * @throws SQLException
     */
    @Test
    public void tableStructureTest() throws SQLException {
        log.info("start ...");

        SyncDb originalSyncDb = new SyncDb();
        originalSyncDb.setId(1);
        originalSyncDb.setName("test column 1");
        originalSyncDb.setDbType("mysql");
        originalSyncDb.setPassword("root");
        originalSyncDb.setUsername("root");
        originalSyncDb.setJdbcUrl("jdbc:mysql://119.3.1.135:3306/bm202012?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&allowPublicKeyRetrieval=true");
        originalSyncDb.setDriver("com.mysql.cj.jdbc.Driver");

        SyncDb syncDb = new SyncDb();
        syncDb.setId(2);
        syncDb.setName("test column 2");
        syncDb.setDbType("mysql");
        syncDb.setPassword("123456");
        syncDb.setUsername("root");
        syncDb.setJdbcUrl("jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&allowPublicKeyRetrieval=true");
        syncDb.setDriver("com.mysql.cj.jdbc.Driver");

        Connection originalConn = JdbcFactory.getConnection(originalSyncDb);
        List<String> originalTables = DBUtil.getTables(originalConn, originalSyncDb.getDatabase());

        Connection connection = JdbcFactory.getConnection(syncDb);
        DBUtil.syncTables(connection,originalConn,syncDb.getDatabase(),originalTables);

        JdbcFactory.shutdown();

        log.info("test complete!");
    }

    /**
     * 表字段同步测试
     */
    @Test
    public void tableColumnTest() throws SQLException {
        log.info("start ...");

        SyncDb originalSyncDb = new SyncDb();
        originalSyncDb.setId(1);
        originalSyncDb.setName("test column 1");
        originalSyncDb.setDbType("mysql");
        originalSyncDb.setPassword("root");
        originalSyncDb.setUsername("root");
        originalSyncDb.setJdbcUrl("jdbc:mysql://119.3.1.135:3306/bm202012?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&allowPublicKeyRetrieval=true");
        originalSyncDb.setDriver("com.mysql.cj.jdbc.Driver");

        SyncDb syncDb = new SyncDb();
        syncDb.setId(2);
        syncDb.setName("test column 2");
        syncDb.setDbType("mysql");
        syncDb.setPassword("123456");
        syncDb.setUsername("root");
        syncDb.setJdbcUrl("jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&allowPublicKeyRetrieval=true");
        syncDb.setDriver("com.mysql.cj.jdbc.Driver");

        Connection originalConn = JdbcFactory.getConnection(originalSyncDb);
        List<SqlColumn> originalColumn = DBUtil.getSqlColumns(originalConn, originalSyncDb.getDatabase(), "device_info");

        // 注意所有字段默认小写
        Connection connection = JdbcFactory.getConnection(syncDb);
        DBUtil.syncTableColumns(connection,"test","device_info",originalColumn);

        JdbcFactory.shutdown();

        log.info("test complete!");
    }

    /**
     * 表索引同步测试
     */
    @Test
    public void tableIndexTest() throws SQLException {
        log.info("start ...");

        SyncDb originalSyncDb = new SyncDb();
        originalSyncDb.setId(1);
        originalSyncDb.setName("test index 1");
        originalSyncDb.setDbType("mysql");
        originalSyncDb.setPassword("root");
        originalSyncDb.setUsername("root");
        originalSyncDb.setJdbcUrl("jdbc:mysql://119.3.1.135:3306/bm202012?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&allowPublicKeyRetrieval=true");
        originalSyncDb.setDriver("com.mysql.cj.jdbc.Driver");

        SyncDb syncDb = new SyncDb();
        syncDb.setId(2);
        syncDb.setName("test index 2");
        syncDb.setDbType("mysql");
        syncDb.setPassword("123456");
        syncDb.setUsername("root");
        syncDb.setJdbcUrl("jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&allowPublicKeyRetrieval=true");
        syncDb.setDriver("com.mysql.cj.jdbc.Driver");

        Connection originalConn = JdbcFactory.getConnection(originalSyncDb);
        Map<String, List<SqlIndex>> originalIndexs = DBUtil.getSqlIndexs(originalConn, originalSyncDb.getDatabase(), "device_info");

        Connection connection = JdbcFactory.getConnection(syncDb);
        DBUtil.syncTableIndex(connection,syncDb.getDatabase(),"device_info",originalIndexs);

        JdbcFactory.shutdown();

        log.info("test complete!");
    }

}
