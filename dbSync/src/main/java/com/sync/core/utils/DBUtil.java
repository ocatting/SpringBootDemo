package com.sync.core.utils;

import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.google.common.base.Joiner;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sync.core.constant.CommonConstant;
import com.sync.core.domain.SqlColumn;
import com.sync.core.domain.SqlIndex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
public final class DBUtil {

    private static final ThreadLocal<ExecutorService> rsExecutors = new ThreadLocal<ExecutorService>() {
        @Override
        protected ExecutorService initialValue() {
            return Executors.newFixedThreadPool(1, new ThreadFactoryBuilder()
                    .setNameFormat("rsExecutors-%d")
                    .setDaemon(true)
                    .build());
        }
    };

    private DBUtil() {
    }

    public static String chooseJdbcUrl(final String dataBaseType,final String driver,
                                       final List<String> jdbcUrls, final String username,
                                       final String password, final List<String> preSql,
                                       final boolean checkSlave) {

        if (null == jdbcUrls || jdbcUrls.isEmpty()) {
            throw new RuntimeException(String.format("您的jdbcUrl的配置信息有错, 因为jdbcUrl[%s]不能为空. 请检查您的配置并作出修改.",
                            StringUtils.join(jdbcUrls, ",")));
        }

        try {
            return RetryUtil.executeWithRetry(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    boolean connOK = false;
                    for (String url : jdbcUrls) {
                        if (StringUtils.isNotBlank(url)) {
                            url = url.trim();
                            if (null != preSql && !preSql.isEmpty()) {
                                connOK = testConnWithoutRetry(dataBaseType,driver,
                                        url, username, password, preSql);
                            } else {
                                connOK = testConnWithoutRetry(dataBaseType,driver,
                                        url, username, password, checkSlave);
                            }
                            if (connOK) {
                                return url;
                            }
                        }
                    }
                    throw new Exception("DataX无法连接对应的数据库，可能原因是：1) 配置的ip/port/database/jdbc错误，无法连接。2) 配置的username/password错误，鉴权失败。请和DBA确认该数据库的连接信息是否正确。");
//                    throw new Exception(DBUtilErrorCode.JDBC_NULL.toString());
                }
            }, 7, 1000L, true);
            //warn: 7 means 2 minutes
        } catch (Exception e) {
            throw new RuntimeException(String.format("数据库连接失败. 因为根据您配置的连接信息,无法从:%s 中找到可连接的jdbcUrl. 请检查您的配置并作出修改.",
                            StringUtils.join(jdbcUrls, ",")), e);
        }
    }

    public static String chooseJdbcUrlWithoutRetry(final String dataBaseType, final String driver,
                                       final List<String> jdbcUrls, final String username,
                                       final String password, final List<String> preSql,
                                       final boolean checkSlave) {

        if (null == jdbcUrls || jdbcUrls.isEmpty()) {
            throw new RuntimeException(String.format("您的jdbcUrl的配置信息有错, 因为jdbcUrl[%s]不能为空. 请检查您的配置并作出修改.",
                            StringUtils.join(jdbcUrls, ",")));
        }

        boolean connOK = false;
        for (String url : jdbcUrls) {
            if (StringUtils.isNotBlank(url)) {
                url = url.trim();
                if (null != preSql && !preSql.isEmpty()) {
                    connOK = testConnWithoutRetry(dataBaseType,driver,
                            url, username, password, preSql);
                } else {
                    try {
                        connOK = testConnWithoutRetry(dataBaseType,driver,
                                url, username, password, checkSlave);
                    } catch (Exception e) {
                        throw new RuntimeException(String.format("数据库连接失败. 因为根据您配置的连接信息,无法从:%s 中找到可连接的jdbcUrl. 请检查您的配置并作出修改.",
                                        StringUtils.join(jdbcUrls, ",")), e);
                    }
                }
                if (connOK) {
                    return url;
                }
            }
        }
        throw new RuntimeException(String.format("数据库连接失败. 因为根据您配置的连接信息,无法从:%s 中找到可连接的jdbcUrl. 请检查您的配置并作出修改.",
                        StringUtils.join(jdbcUrls, ",")));
    }

    /**
     * 检查slave的库中的数据是否已到凌晨00:00
     * 如果slave同步的数据还未到00:00返回false
     * 否则范围true
     *
     * @author ZiChi
     * @version 1.0 2014-12-01
     */
    private static boolean isSlaveBehind(Connection conn) {
        try {
            ResultSet rs = query(conn, "SHOW VARIABLES LIKE 'read_only'");
            if (DBUtil.asyncResultSetNext(rs)) {
                String readOnly = rs.getString("Value");
                if ("ON".equalsIgnoreCase(readOnly)) { //备库
                    ResultSet rs1 = query(conn, "SHOW SLAVE STATUS");
                    if (DBUtil.asyncResultSetNext(rs1)) {
                        String ioRunning = rs1.getString("Slave_IO_Running");
                        String sqlRunning = rs1.getString("Slave_SQL_Running");
                        long secondsBehindMaster = rs1.getLong("Seconds_Behind_Master");
                        if ("Yes".equalsIgnoreCase(ioRunning) && "Yes".equalsIgnoreCase(sqlRunning)) {
                            ResultSet rs2 = query(conn, "SELECT TIMESTAMPDIFF(SECOND, CURDATE(), NOW())");
                            DBUtil.asyncResultSetNext(rs2);
                            long secondsOfDay = rs2.getLong(1);
                            return secondsBehindMaster > secondsOfDay;
                        } else {
                            return true;
                        }
                    } else {

                        log.warn("SHOW SLAVE STATUS has no result");
                    }
                }
            } else {
                log.warn("SHOW VARIABLES like 'read_only' has no result");
            }
        } catch (Exception e) {
            log.warn("checkSlave failed, errorMessage:[{}].", e.getMessage());
        }
        return false;
    }

    /**
     * 检查表是否存在
     * @param connection 连接
     * @param tableName 表名称
     * @return boolean 结果
     * @throws SQLException 执行异常
     */
    public static boolean existsTable(Connection connection,String catalog,String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        String schemaName = null;
        if(tableName.contains(".")){
            String[] split = tableName.split("\\.");
            schemaName = split[0];
            tableName = split[1];
        }
        ResultSet resultSet = metaData.getTables(catalog, schemaName, tableName, null);
        boolean next = resultSet.next();
        DBUtil.closeDBResources(resultSet,null,connection);
        return next;
    }



    /**
     * Get direct JDBC connection
     * <p/>
     * if connecting failed, try to connect for MAX_TRY_TIMES times
     * <p/>
     * NOTE: In DataX, we don't need connection pool in fact
     */
    public static Connection getConnection(final String dataBaseType ,final String driver,
                                           final String jdbcUrl, final String username, final String password) {

        return getConnection(dataBaseType,driver, jdbcUrl, username, password, String.valueOf(CommonConstant.SOCKET_TIMEOUT_IN_SECOND * 1000));
    }

    /**
     *
     * @param dataBaseType
     * @param jdbcUrl
     * @param username
     * @param password
     * @param socketTimeout 设置socketTimeout，单位ms，String类型
     * @return
     */
    public static Connection getConnection(final String dataBaseType ,final String driver,
                                           final String jdbcUrl, final String username, final String password, final String socketTimeout) {

        try {
            return RetryUtil.executeWithRetry(new Callable<Connection>() {
                @Override
                public Connection call() throws Exception {
                    return DBUtil.connect(dataBaseType,driver, jdbcUrl, username,
                            password, socketTimeout);
                }
            }, 3, 1000L, true);
        } catch (Exception e) {
            throw new RuntimeException(String.format("数据库连接失败. 因为根据您配置的连接信息:%s获取数据库连接失败. 请检查您的配置并作出修改.", jdbcUrl), e);
        }
    }

    /**
     * Get direct JDBC connection
     * <p/>
     * if connecting failed, try to connect for MAX_TRY_TIMES times
     * <p/>
     * NOTE: In DataX, we don't need connection pool in fact
     */
    public static Connection getConnectionWithoutRetry(final String dataBaseType,final String driver,
                                                       final String jdbcUrl, final String username, final String password) {
        return getConnectionWithoutRetry(dataBaseType,driver, jdbcUrl, username,
                password, String.valueOf(CommonConstant.SOCKET_TIMEOUT_IN_SECOND * 1000));
    }

    public static Connection getConnectionWithoutRetry(final String dataBaseType,final String driver,
                                                       final String jdbcUrl, final String username, final String password, String socketTimeout) {
        return DBUtil.connect(dataBaseType,driver, jdbcUrl, username,
                password, socketTimeout);
    }

    private static synchronized Connection connect(String dataBaseType,String driver,
                                                   String url, String user, String pass) {
        return connect(dataBaseType,driver, url, user, pass, String.valueOf(CommonConstant.SOCKET_TIMEOUT_IN_SECOND * 1000));
    }

    private static synchronized Connection connect(String dataBaseType,String driver,
                                                   String url, String user, String pass, String socketTimeout) {

        Properties prop = new Properties();
        prop.put("user", user);
        prop.put("password", pass);

        if ( DataBaseType.Oracle.getTypeName().equals(dataBaseType)) {
            //oracle.net.READ_TIMEOUT for jdbc versions < 10.1.0.5 oracle.jdbc.ReadTimeout for jdbc versions >=10.1.0.5
            // unit ms
            prop.put("oracle.jdbc.ReadTimeout", socketTimeout);
        }

        return connect(driver, url, prop);
    }

    private static synchronized Connection connect(String driver,String url, Properties prop) {
        try {
            Class.forName(driver);
            DriverManager.setLoginTimeout(CommonConstant.TIMEOUT_SECONDS);
            return DriverManager.getConnection(url, prop);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建SQL语句
     * @param tableName 表名
     * @param columns 拥有字段
     * @param whereSql where 条件
     * @param defaultSql 默认完整sql
     * @return
     */
    public static String createQuerySql(String tableName ,List<String> columns,String whereSql,String defaultSql){
        if(!StringUtils.isEmpty(defaultSql)){
            return defaultSql;
        }
        StringBuilder sb = new StringBuilder("select ");

        List<String> list = new ArrayList<>(columns.size());
        for (String column : columns) {
            list.add("`"+column+"`");
        }

        sb.append(StringUtils.join(list, ","));
        sb.append(" from ").append(tableName);

        if(!StringUtils.isEmpty(whereSql)){
            sb.append(" where ").append(whereSql);
        }
        return sb.toString();
    }

    /**
     * a wrapped method to execute select-like sql statement .
     *
     * @param conn Database connection .
     * @param sql  sql statement to be executed
     * @return a {@link ResultSet}
     * @throws SQLException if occurs SQLException.
     */
    public static ResultSet query(Connection conn, String sql, int fetchSize)
            throws SQLException {
        // 默认3600 s 的query Timeout
        return query(conn, sql, fetchSize, CommonConstant.SOCKET_TIMEOUT_IN_SECOND);
    }

    /**
     * a wrapped method to execute select-like sql statement .
     *
     * @param conn         Database connection .
     * @param sql          sql statement to be executed
     * @param fetchSize
     * @param queryTimeout unit:second
     * @return
     * @throws SQLException
     */
    public static ResultSet query(Connection conn, String sql, int fetchSize, int queryTimeout)
            throws SQLException {
        // make sure autocommit is off
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(fetchSize);
        stmt.setQueryTimeout(queryTimeout);
        return query(stmt, sql);
    }

    public static ResultSet query(Connection conn, String sql)
            throws SQLException {
        Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
        //默认3600 seconds
        stmt.setQueryTimeout(CommonConstant.SOCKET_TIMEOUT_IN_SECOND);
        return query(stmt, sql);
    }

    /**
     * a wrapped method to execute select-like sql statement .
     *
     * @param stmt {@link Statement}
     * @param sql  sql statement to be executed
     * @return a {@link ResultSet}
     * @throws SQLException if occurs SQLException.
     */
    public static ResultSet query(Statement stmt, String sql)
            throws SQLException {
        return stmt.executeQuery(sql);
    }

    public static void executeSql(Connection conn,String sql) throws SQLException {
        if(conn == null){
            return;
        }
        Statement statement = null;
        try{
            conn.setAutoCommit(true);
            statement = conn.createStatement();
            executeSqlWithoutResultSet(statement,sql);
        } finally {
            DBUtil.closeDBResources(statement, conn);
        }
    }

    public static void executeSqlWithoutResultSet(Statement stmt, String sql)
            throws SQLException {
        stmt.execute(sql);
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }
            catch (SQLException ex) {
                log.trace("Could not close JDBC Statement", ex);
            }
            catch (Throwable ex) {
                // We don't trust the JDBC driver: It might throw RuntimeException or Error.
                log.trace("Unexpected exception on closing JDBC Statement", ex);
            }
        }
    }

    /**
     * Close {@link ResultSet}, {@link Statement} referenced by this
     * {@link ResultSet}
     *
     * @param rs {@link ResultSet} to be closed
     * @throws IllegalArgumentException
     */
    public static void closeResultSet(ResultSet rs) {
        try {
            if (null != rs) {
                Statement stmt = rs.getStatement();
                if (null != stmt) {
                    stmt.close();
                    stmt = null;
                }
                rs.close();
            }
            rs = null;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void closeDBResources(ResultSet rs, Statement stmt,
                                        Connection conn) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }

        if (null != stmt) {
            try {
                stmt.close();
            } catch (SQLException ignored) {
            }
        }

        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void closeDBResources(Statement stmt, Connection conn) {
        closeDBResources(null, stmt, conn);
    }

    /**
     * 获取所有表名称
     * @param conn 连接
     * @return
     * @throws SQLException
     */
    public static List<String> getTables(Connection conn,String catalog) throws SQLException {
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        ResultSet tables = databaseMetaData.getTables(catalog, null, "%", null);
        ArrayList<String> tablesList = new ArrayList<>();
        while (tables.next()) {
            tablesList.add(tables.getString("TABLE_NAME"));
        }
        return tablesList;
    }

    public static String getDdl(Connection conn ,String table) throws SQLException {
        Assert.hasLength(table,"table is null");

        String sql = "SHOW CREATE TABLE " + table;
        conn.setAutoCommit(false);
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        String result = null;
        while (resultSet.next()){
            result = resultSet.getString(2);
        }
        return result;
    }

    public static String getDdl(Connection conn,String catalog,String table) throws SQLException {

        Assert.hasLength(table,"table is null");

        DatabaseMetaData metaData = conn.getMetaData();
        StringBuilder sb = new StringBuilder(1024);

        String schema = null;
        if (table.contains(".")) {
            String[] split = table.split("\\.");
            schema = split[0];
            table = split[1];
        }

        ResultSet resultSet = metaData.getColumns(catalog, schema, table, null);

        sb.append("create table `").append(table).append("` (\n");

        int i = 0;
        while (resultSet.next()) {
            i++;
            String columnName = resultSet.getString(4);
            String columnType = resultSet.getString(6);
            String nullAble = resultSet.getString(18);
            String isAutoincrement = resultSet.getString(23);
            String remark = resultSet.getString(12);
            String columnDef = resultSet.getString(13);

            int dataType = resultSet.getInt(5);
            int columnSize = resultSet.getInt(7);
            int digitSize = resultSet.getInt(9);

            String columnTypeText = "";
            if (columnType.equals("TEXT") || columnType.equals("CLOB") || columnType.equals("BLOB")) {
                columnTypeText = columnType;
            } else if (dataType == Types.VARCHAR || dataType == Types.CHAR) {
                // varchar || char
                columnTypeText = columnType + "(" + columnSize + ")";
            } else if (dataType == Types.DECIMAL || dataType == Types.NUMERIC) {
                columnTypeText = columnType + "(" + columnSize + "," + digitSize + ")";
            } else {
                columnTypeText = columnType;
            }

            if (i == 1) {
                sb.append("`").append(columnName).append("`");
                sb.append(" ");
                sb.append(columnTypeText).append(",");;
            } else {
                sb.append("`").append(columnName).append("`");
                sb.append(" ");
                sb.append(columnTypeText);
            }
            sb.append(" ");

            if("YES".equals(nullAble)){
                sb.append("NOT NULL ");
            }

            if(columnDef != null){
                sb.append("DEFAULT '").append(columnDef).append("'");
            }

            if("YES".equals(isAutoincrement)){
                sb.append(" AUTO_INCREMENT ");
            }

            if (remark != null) {
                remark = remark.replace(";", "|").replace("'", "");
                sb.append(" COMMENT '").append(remark).append("'");
            }
            sb.append(",");
            sb.append(" \n");
        }

        // 添加主键或分区键信息
        ResultSet primaryKeys = metaData.getPrimaryKeys(catalog, schema, table);
        ArrayList<String> pks = new ArrayList<>();
        while (primaryKeys.next()) {
            String pkColumnName = primaryKeys.getString(4);
            pks.add("`"+pkColumnName+"`");
        }
        if (!pks.isEmpty()) {
            sb.append(" PRIMARY KEY (").append(Joiner.on(",").join(pks)).append(") USING BTREE,\n");
        }

        // 添加索引信息
        ResultSet indexInfo = metaData.getIndexInfo(catalog, schema, table, false, false);
        while (indexInfo.next()) {
            boolean nonUnique = indexInfo.getBoolean(4);
            if(nonUnique){
                sb.append(" UNIQUE");
            }
            String indexName = indexInfo.getString(6);
            String columnName = indexInfo.getString(9);
            short type = indexInfo.getShort(7);
            System.out.println("indexName:"+indexName+",columnName:"+columnName+",type:"+type);
            sb.append(" KEY `").append(indexName).append("`").append(" (`").append(columnName).append("`) USING BTREE \n");
        }

        sb.append(")");

        // 统一添加分号结尾
        sb.append(";\n");
        String tableRemark = null;

        ResultSet tbResultSet = metaData.getTables(catalog, schema, table, null);
        if (tbResultSet.next()) {
            tableRemark = tbResultSet.getString(5);
        }
        if (tableRemark != null && !tableRemark.isEmpty()) {
            tableRemark = tableRemark.replace(";", "").replace("'", "");
            sb.append("alter table ").append(table).append(" comment '").append(tableRemark).append("';\n");
        }
        return sb.toString();
    }

    public static List<String> getTableColumnsByConn(Connection conn, String tableName,String customSql) {
        if(StringUtils.isEmpty(customSql)){
            return getTableColumnsByConn(conn,tableName);
        }
        return getTableColumnsByConnSql(conn,customSql);
    }

    public static List<String> getTableColumnsByConn(Connection conn, String tableName) {
        List<String> columns = new ArrayList<String>();
        Statement statement = null;
        ResultSet rs = null;
        String queryColumnSql = null;
        try {
            statement = conn.createStatement();
            queryColumnSql = String.format("select * from %s where 1=2",tableName);
            rs = statement.executeQuery(queryColumnSql);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            for (int i = 0, len = rsMetaData.getColumnCount(); i < len; i++) {
                columns.add(rsMetaData.getColumnName(i + 1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(queryColumnSql,e);
        } finally {
            DBUtil.closeDBResources(rs,statement,conn);
        }
        return columns;
    }

    public static List<String> getTableColumnsByConnSql(Connection conn, String customSql) {
        List<String> columns = new ArrayList<String>();
        Statement statement = null;
        ResultSet rs = null;

        String queryColumnSql = null;
        try {
            statement = conn.createStatement();
            // 注意这里不支持复杂的SQL语句
            queryColumnSql = customSql.split("where")[0] + " where 1=2";
            rs = statement.executeQuery(queryColumnSql);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            for (int i = 0, len = rsMetaData.getColumnCount(); i < len; i++) {
                columns.add(rsMetaData.getColumnName(i + 1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(queryColumnSql,e);
        } finally {
            DBUtil.closeDBResources(statement, conn);
        }
        return columns;
    }

    /**
     * 同步表结构
     * @param conn 待同步表连接
     * @param originalConn 源对照表连接，用于获取ddl
     * @param catalog 待同步数据库
     * @param original 源对照表
     * @throws SQLException 连接/查询异常
     */
    public static void syncTables(Connection conn,Connection originalConn,String catalog,List<String> original) throws SQLException {

        // 获取待同步的表字段
        List<String> tables = getTables(conn, catalog);
        log.info("sync tables ,catalog:[{}] ,original tables :{}, mapping tables:{}",catalog,original,tables);

        List<String> addTables = tables.stream().filter(original::contains).collect(Collectors.toList());
        log.info("sync tables ,catalog:[{}] ,original tables :{}",catalog,tables);

        for (String addTable : addTables) {
            String ddl = getDdl(originalConn, addTable);
            if(ddl == null){
                log.info("sync tables ,catalog:[{}] table name `{}` does not exist ",catalog,addTable);
                continue;
            }
            log.info("sync tables ,catalog:[{}] ddl:\n{} ",catalog,ddl);
            executeSql(conn,ddl);
        }
    }

    private boolean contains(List<SqlColumn> original,List<SqlColumn> add){
        for (SqlColumn sqlColumn : original) {
            for (SqlColumn column : add) {
                if (sqlColumn.getColumnName().equals(column.getColumnName())) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * 同步表字段
     * @param conn 待同步表连接
     * @param catalog 待同步数据库
     * @param table 待同步表名
     * @param original 源对照表字段
     * @throws SQLException 连接/执行异常
     */
    public static void syncTableColumns(Connection conn,String catalog,String table,List<SqlColumn> original) throws SQLException {

        Assert.hasLength(table,"table is null");
        Assert.notNull(original,"original sqlColumn is null");

        List<SqlColumn> sqlColumns = getSqlColumns(conn, catalog, table);

        // 新增
        List<SqlColumn> add = original.stream().filter((o) -> {
            for (SqlColumn sqlColumn : sqlColumns) {
                if (o.getColumnName().equalsIgnoreCase(sqlColumn.getColumnName())){
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());

        log.debug("sync table columns ,catalog:[{}] table name `{}` add SqlColumn list:{}",catalog,table,add);

        // 更新
        List<SqlColumn> change = original.stream().filter((o) -> {
            for (SqlColumn sqlColumn : sqlColumns) {
                if (o.getColumnName().equalsIgnoreCase(sqlColumn.getColumnName())) {
                    if (sqlColumn.equals(o)) {
                        return false;
                    }
                }
            }
            // 没有找到也过滤
            return false;
        }).collect(Collectors.toList());

        log.debug("sync table columns ,catalog:[{}] table name `{}` change SqlColumn list:{}",catalog,table,change);

        // 删除
        List<SqlColumn> del = sqlColumns.stream().filter((o) -> {
            for (SqlColumn sqlColumn : original) {
                if (sqlColumn.getColumnName().equalsIgnoreCase(o.getColumnName())) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());

        log.debug("sync table columns ,catalog:[{}] table name `{}` del SqlColumn list:{}",catalog,table,del);

        List<String> alertSqls = new ArrayList<>();

        for (SqlColumn sqlColumn : add) {
            alertSqls.add(alertColumnSql(sqlColumn,"add"));
        }
        for (SqlColumn sqlColumn : change){
            Optional<SqlColumn> any = original.stream().filter(e -> e.getColumnName().equals(sqlColumn.getColumnName())).findAny();
            any.ifPresent(column -> alertSqls.add(alertColumnSql(sqlColumn, "change")));
        }

        for (SqlColumn sqlColumn : del){
            alertSqls.add(alertColumnSql(sqlColumn,"drop"));
        }
        for (String alertSql : alertSqls) {
            log.info("sync table columns ,catalog:[{}] table name `{}` execute SQL: {}",catalog,table,alertSql);
            executeSql(conn,alertSql);
        }
    }

    /**
     * 同步表索引。(慎用会长时间锁表）关于 mysql 特殊点 里面没有 sub_part 字段
     * @param conn 待同步连接
     * @param catalog 待同步数据库
     * @param table 待同步表名
     * @param original 源对照表索引
     */
    public static void syncTableIndex(Connection conn,String catalog,String table,Map<String,List<SqlIndex>> original) throws SQLException {
        Assert.hasLength(table,"table is null");
        Assert.notNull(original,"original sqlColumn is null");

        Map<String,List<SqlIndex>> indexs = getSqlIndexs(conn, catalog, table);

        log.info("sync table index ,catalog:[{}] table name `{}` original index: {} ,mapping index: {}",catalog,table,original,indexs);

        List<String> alertSqls = new ArrayList<>();
        // 新增
        for (Map.Entry<String, List<SqlIndex>> entry : original.entrySet()) {
            if (!indexs.containsKey(entry.getKey())) {
                String add = alertIndexSql(entry.getValue(), "add", null);
                if(add != null){
                    alertSqls.add(add);
                }
            }
        }
        // 更新
        for (Map.Entry<String, List<SqlIndex>> entry : original.entrySet()) {
            if (indexs.containsKey(entry.getKey())) {
                List<SqlIndex> news = indexs.get(entry.getKey());
                if(entry.getValue().size() != news.size()){
                    String modify = alertIndexSql(entry.getValue(), "modify", entry.getKey());
                    if(modify != null){
                        alertSqls.add(modify);
                    }
                }
            }
        }
        // 不删除任何索引
        // 执行索引语句
        for (String alertSql : alertSqls) {
            log.info("sync table index ,catalog:[{}] table name `{}` execute SQL: {}",catalog,table,alertSql);
            executeSql(conn,alertSql);
        }
    }

    /**
     * 索引字段 不完全支持 mysql
     * @param conn
     * @param catalog
     * @param table
     * @return
     * @throws SQLException
     */
    public static Map<String,List<SqlIndex>> getSqlIndexs(Connection conn,String catalog,String table) throws SQLException {

        Assert.hasLength(table,"table is null");

        DatabaseMetaData metaData = conn.getMetaData();
        String schema = null;
        if (table.contains(".")) {
            String[] split = table.split("\\.");
            schema = split[0];
            table = split[1];
        }

        Map<String,List<SqlIndex>> indexs = new HashMap<>();

        ResultSet indexInfo = metaData.getIndexInfo(catalog, schema, table, false, false);
        while (indexInfo.next()) {
            SqlIndex sqlIndex = getSqlIndex(indexInfo);
            if(indexs.containsKey(sqlIndex.getIndexName())){
                List<SqlIndex> sqlIndices = indexs.get(sqlIndex.getIndexName());
                sqlIndices.add(sqlIndex);
            } else {
                List<SqlIndex> sub = new ArrayList<>();
                sub.add(sqlIndex);
                indexs.put(sqlIndex.getIndexName(),sub);
            }
        }
        return indexs;
    }

    public static SqlIndex getSqlIndex(ResultSet indexInfo) throws SQLException {

        String tableName = indexInfo.getString(3);
        boolean nonUnique = indexInfo.getBoolean(4);
        String indexQualifier = indexInfo.getString(5);
        String indexName = indexInfo.getString(6);
        String columnName = indexInfo.getString(9);
        short type = indexInfo.getShort(7);
        short ordinalPosition = indexInfo.getShort(8);

        SqlIndex sqlIndex = new SqlIndex();
        sqlIndex.setTableName(tableName);
        sqlIndex.setNonUnique(nonUnique);
        sqlIndex.setIndexQualifier(indexQualifier);
        sqlIndex.setIndexName(indexName);
        sqlIndex.setColumnName(columnName);
        sqlIndex.setType(type);
        sqlIndex.setOrdinalPosition(ordinalPosition);
        return sqlIndex;
    }


    /**
     * 获取字段描述，忽略字段大小写
     * @param conn
     * @param catalog
     * @param table
     * @return
     * @throws SQLException
     */
    public static List<SqlColumn> getSqlColumns(Connection conn,String catalog,String table) throws SQLException {
        Assert.hasLength(table,"table is null");

        DatabaseMetaData metaData = conn.getMetaData();

        String schema = null;
        if (table.contains(".")) {
            String[] split = table.split("\\.");
            schema = split[0];
            table = split[1];
        }

        List<SqlColumn> sqlColumns = new ArrayList<>();

        ResultSet resultSet = metaData.getColumns(catalog, schema, table, null);

        while (resultSet.next()) {
            SqlColumn sqlColumn;
            if(sqlColumns.isEmpty()){
                sqlColumn = getSqlColumn(resultSet, null);
            } else {
                sqlColumn = getSqlColumn(resultSet, sqlColumns.get(sqlColumns.size() - 1));
            }
            sqlColumns.add(sqlColumn);
        }
        return sqlColumns;
    }

    public static SqlColumn getSqlColumn(ResultSet resultSet,SqlColumn lastSqlColumn) throws SQLException {

        String tableName = resultSet.getString(3);
        String columnName = resultSet.getString(4);
        String columnType = resultSet.getString(6);
        String nullAble = resultSet.getString(18);
        String isAutoincrement = resultSet.getString(23);
        String remark = resultSet.getString(12);
        String columnDef = resultSet.getString(13);

        int dataType = resultSet.getInt(5);
        int columnSize = resultSet.getInt(7);
        int digitSize = resultSet.getInt(9);

        String columnTypeText = "";
        if (columnType.equals("TEXT") || columnType.equals("CLOB") || columnType.equals("BLOB")) {
            columnTypeText = columnType;
        } else if (dataType == Types.VARCHAR || dataType == Types.CHAR) {
            // varchar || char
            columnTypeText = columnType + "(" + columnSize + ")";
        } else if (dataType == Types.DECIMAL || dataType == Types.NUMERIC) {
            columnTypeText = columnType + "(" + columnSize + "," + digitSize + ")";
        } else {
            columnTypeText = columnType;
        }

        SqlColumn sqlColumn = new SqlColumn();
        sqlColumn.setTableName(tableName);
        sqlColumn.setColumnName(columnName.toLowerCase());
        sqlColumn.setColumnType(columnType);
        sqlColumn.setNullAble(nullAble);
        sqlColumn.setIsAutoincrement(isAutoincrement);
        sqlColumn.setRemark(remark);
        sqlColumn.setColumnDef(columnDef);
        sqlColumn.setDataType(dataType);
        sqlColumn.setColumnSize(columnSize);
        sqlColumn.setDigitSize(digitSize);
        sqlColumn.setColumnTypeText(columnTypeText);

        if(lastSqlColumn != null){
            sqlColumn.setAfter(lastSqlColumn.getColumnName());
        }

        return sqlColumn;
    }

    /**
     * alert sql add ,modify ,drop
     *
     * "add"    alter table #TABLE_NAME# add COLUMN #COLUMN_NAME# VARCHAR(20) DEFAULT NULL COMMENT '注释';
     *
     * "change" alter table #TABLE_NAME# change column #COLUMN_NAME# decimal(10,1) DEFAULT NULL COMMENT '注释';
     *
     * "drop"   alter table #TABLE_NAME# DROP COLUMN #COLUMN_NAME#;
     * @param column 字段
     * @return
     */
    public static String alertColumnSql(SqlColumn column, String operate){
        Assert.notNull(column,"alertSqls column is null");
        Assert.notNull(operate,"alertSqls operate is null");

        StringBuilder sb = new StringBuilder();
        sb.append("ALTER TABLE `").append(column.getTableName()).append("` ");

        if("add".equalsIgnoreCase(operate)){
            sb.append("add ");
        } else if ("change".equalsIgnoreCase(operate)){
            sb.append("change ");
        } else if ("drop".equalsIgnoreCase(operate)){
            sb.append("drop ").append("`").append(column.getColumnName()).append("`").append(" ;");
            return sb.toString();
        } else {
            throw new IllegalArgumentException("operate exception :"+operate);
        }
        sb.append("`").append(column.getColumnName()).append("` ");
        sb.append(column.getColumnTypeText()).append(" ");
        if(StringUtils.equals(column.getNullAble(),"NO")){
            sb.append("NOT NULL ");
        }
        if(!StringUtils.isBlank(column.getColumnDef())){
            sb.append("DEFAULT ").append(column.getColumnDef()).append(" ");
        }
        if(!StringUtils.isBlank(column.getRemark())){
            sb.append("COMMENT ").append("'").append(column.getRemark()).append("' ");
        }
        if(!StringUtils.isBlank(column.getAfter())){
            sb.append("AFTER `").append(column.getAfter()).append("` ");
        }
        sb.append(";");
        return sb.toString();
    }

    public static String alertIndexSql(List<SqlIndex> sqlIndexList,String operate, String modifyOld) {
        Assert.notNull(sqlIndexList,"alertSqls column is null");
        Assert.notNull(operate,"alertSqls operate is null");

        if(sqlIndexList.isEmpty()){
            return null;
        }

        SqlIndex sqlIndex = sqlIndexList.get(0);

        // 不对主键进行任何操作
        if("PRIMARY".equalsIgnoreCase(sqlIndex.getIndexName())){
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ALTER TABLE `").append(sqlIndex.getTableName()).append("` ");
        if("add".equalsIgnoreCase(operate)){
            sb.append("add");
        } else if ("modify".equalsIgnoreCase(operate)) {
            // mysql 没有真正意义上的修改索引，先删除再添加;
            String drop = "DROP INDEX `"+sqlIndex.getIndexName()+"` on `"+sqlIndex.getIndexName()+"`;";
            sb.insert(0,drop);
        } else if ("del".equalsIgnoreCase(operate)) {
            String drop = "DROP INDEX `"+sqlIndex.getIndexName()+"` on `"+sqlIndex.getIndexName()+"`;";
            return sb.toString();
        }else {
            throw new IllegalArgumentException("operate exception :"+operate);
        }

        if(!sqlIndex.isNonUnique()){
            sb.append(" UNIQUE");
        }

        sb.append(" INDEX ").append("`").append(sqlIndex.getIndexName()).append("`");
        sb.append("(");
        sb.append(Joiner.on(",").join(sqlIndexList.stream().map(o->"`"+o.getColumnName()+"`").collect(Collectors.toList())));
        sb.append(")");

        return sb.toString();

    }

    /**
     * @return Left:ColumnName Middle:ColumnType Right:ColumnTypeName
     */
    public static Triple<List<String>, List<Integer>, List<String>> getColumnMetaData (
            Connection conn, String tableName, String column) {
        Statement statement = null;
        ResultSet rs = null;

        Triple<List<String>, List<Integer>, List<String>> columnMetaData = new ImmutableTriple<List<String>, List<Integer>, List<String>>(
                new ArrayList<>(), new ArrayList<>(),new ArrayList<>());
        try {
            statement = conn.createStatement();
            String queryColumnSql = "select " + column + " from " + tableName
                    + " where 1=2";

            rs = statement.executeQuery(queryColumnSql);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            for (int i = 0, len = rsMetaData.getColumnCount(); i < len; i++) {

                columnMetaData.getLeft().add(rsMetaData.getColumnName(i + 1));
                columnMetaData.getMiddle().add(rsMetaData.getColumnType(i + 1));
                columnMetaData.getRight().add(rsMetaData.getColumnTypeName(i + 1));
            }
            return columnMetaData;

        } catch (SQLException e) {
            throw new RuntimeException(String.format("获取表:%s 的字段的元信息时失败. 请联系 DBA 核查该库、表信息.", tableName), e);
        } finally {
            DBUtil.closeDBResources(rs, statement, null);
        }
    }

    public static boolean testConnWithoutRetry(String dataBaseType,String driver,
                                               String url, String user, String pass, boolean checkSlave){
        Connection connection = null;

        try {
            connection = connect(dataBaseType,driver, url, user, pass);
            if (connection != null) {
                if (dataBaseType.equals(DataBaseType.MySql) && checkSlave) {
                    //dataBaseType.MySql
                    boolean connOk = !isSlaveBehind(connection);
                    return connOk;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            log.warn("test connection of [{}] failed, for {}.", url,e.getMessage());
        } finally {
            DBUtil.closeDBResources(null, connection);
        }
        return false;
    }

    public static boolean testConnWithoutRetry(String dataBaseType,String driver,
                                               String url, String user, String pass, List<String> preSql) {
        Connection connection = null;
        try {
            connection = connect(dataBaseType,driver, url, user, pass);
            if (null != connection) {
                for (String pre : preSql) {
                    if (!doPreCheck(connection, pre)) {
                        log.warn("doPreCheck failed.");
                        return false;
                    }
                }
                return true;
            }
        } catch (Exception e) {
            log.warn("test connection of [{}] failed, for {}.", url,
                    e.getMessage());
        } finally {
            DBUtil.closeDBResources(null, connection);
        }

        return false;
    }

    public static boolean isOracleMaster(final String driver, final String url, final String user, final String pass) {
        try {
            return RetryUtil.executeWithRetry(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    Connection conn = null;
                    try {
                        conn = connect(DataBaseType.Oracle.getTypeName(),driver, url, user, pass);
                        ResultSet rs = query(conn, "select DATABASE_ROLE from V$DATABASE");
                        if (DBUtil.asyncResultSetNext(rs, 5)) {
                            String role = rs.getString("DATABASE_ROLE");
                            return "PRIMARY".equalsIgnoreCase(role);
                        }
                        throw new RuntimeException(String.format("select DATABASE_ROLE from V$DATABASE failed,请检查您的jdbcUrl:%s.", url));
                    } finally {
                        DBUtil.closeDBResources(null, conn);
                    }
                }
            }, 3, 1000L, true);
        } catch (Exception e) {
            throw new RuntimeException(String.format("select DATABASE_ROLE from V$DATABASE failed, url: %s", url), e);
        }
    }



    private static boolean doPreCheck(Connection conn, String pre) {
        ResultSet rs = null;
        try {
            rs = query(conn, pre);

            int checkResult = -1;
            if (DBUtil.asyncResultSetNext(rs)) {
                checkResult = rs.getInt(1);
                if (DBUtil.asyncResultSetNext(rs)) {
                    log.warn(
                            "pre check failed. It should return one result:0, pre:[{}].",
                            pre);
                    return false;
                }

            }

            if (0 == checkResult) {
                return true;
            }

            log.warn(
                    "pre check failed. It should return one result:0, pre:[{}].",
                    pre);
        } catch (Exception e) {
            log.warn("pre check failed. pre:[{}], errorMessage:[{}].", pre,
                    e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
        }
        return false;
    }

    public static void sqlValid(String sql, String dataBaseType){
        SQLStatementParser statementParser = SQLParserUtils.createSQLStatementParser(sql,dataBaseType);
        statementParser.parseStatementList();
    }

    /**
     * 异步获取resultSet的next(),注意，千万不能应用在数据的读取中。只能用在meta的获取
     * @param resultSet 结果集
     * @return success/fail
     */
    public static boolean asyncResultSetNext(final ResultSet resultSet) {
        return asyncResultSetNext(resultSet, 3600);
    }

    public static boolean asyncResultSetNext(final ResultSet resultSet, int timeout) {
        Future<Boolean> future = rsExecutors.get().submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return resultSet.next();
            }
        });
        try {
            return future.get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException( "异步获取ResultSet失败", e);
        }
    }

    /**
     * 检查表是否具有insert 权限
     * insert on *.* 或者 insert on database.* 时验证通过
     * 当insert on database.tableName时，确保tableList中的所有table有insert 权限，验证通过
     * 其它验证都不通过
     *
     * @author ZiChi
     * @version 1.0 2015-01-28
     */
    public static boolean hasInsertPrivilege(String dataBaseType,String driver, String jdbcURL, String userName, String password, List<String> tableList) {
        /*准备参数*/

        String[] urls = jdbcURL.split("/");
        String dbName;
        if (urls.length != 0) {
            dbName = urls[3];
        }else{
            return false;
        }

        String dbPattern = "`" + dbName + "`.*";
        Collection<String> tableNames = new HashSet<String>(tableList.size());
        tableNames.addAll(tableList);

        Connection connection = connect(dataBaseType,driver, jdbcURL, userName, password);
        try {
            ResultSet rs = query(connection, "SHOW GRANTS FOR " + userName);
            while (DBUtil.asyncResultSetNext(rs)) {
                String grantRecord = rs.getString("Grants for " + userName + "@%");
                String[] params = grantRecord.split("\\`");
                if (params != null && params.length >= 3) {
                    String tableName = params[3];
                    if (params[0].contains("INSERT") && !tableName.equals("*") && tableNames.contains(tableName))
                        tableNames.remove(tableName);
                } else {
                    if (grantRecord.contains("INSERT") ||grantRecord.contains("ALL PRIVILEGES")) {
                        if (grantRecord.contains("*.*")) {
                            return true;
                        } else if (grantRecord.contains(dbPattern)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Check the database has the Insert Privilege failed, errorMessage:[{}]", e.getMessage());
        }
        if (tableNames.isEmpty())
            return true;
        return false;
    }


    public static boolean checkInsertPrivilege(String dataBaseType,String driver, String jdbcURL, String userName, String password, List<String> tableList) {
        Connection connection = connect(dataBaseType,driver, jdbcURL, userName, password);
        String insertTemplate = "insert into %s(select * from %s where 1 = 2)";

        boolean hasInsertPrivilege = true;
        Statement insertStmt = null;
        for(String tableName : tableList) {
            String checkInsertPrivilegeSql = String.format(insertTemplate, tableName, tableName);
            try {
                insertStmt = connection.createStatement();
                executeSqlWithoutResultSet(insertStmt, checkInsertPrivilegeSql);
            } catch (Exception e) {
                if(DataBaseType.Oracle.getTypeName().equals(dataBaseType)) {
                    if(e.getMessage() != null && e.getMessage().contains("insufficient privileges")) {
                        hasInsertPrivilege = false;
                        log.warn("User [" + userName +"] has no 'insert' privilege on table[" + tableName + "], errorMessage:[{}]", e.getMessage());
                    }
                } else {
                    hasInsertPrivilege = false;
                    log.warn("User [" + userName + "] has no 'insert' privilege on table[" + tableName + "], errorMessage:[{}]", e.getMessage());
                }
            }
        }
        try {
            connection.close();
        } catch (SQLException e) {
            log.warn("connection close failed, " + e.getMessage());
        }
        return hasInsertPrivilege;
    }

    public static boolean checkDeletePrivilege(String dataBaseType,String driver,String jdbcURL, String userName, String password, List<String> tableList) {
        Connection connection = connect(dataBaseType,driver, jdbcURL, userName, password);
        String deleteTemplate = "delete from %s WHERE 1 = 2";

        boolean hasInsertPrivilege = true;
        Statement deleteStmt = null;
        for(String tableName : tableList) {
            String checkDeletePrivilegeSQL = String.format(deleteTemplate, tableName);
            try {
                deleteStmt = connection.createStatement();
                executeSqlWithoutResultSet(deleteStmt, checkDeletePrivilegeSQL);
            } catch (Exception e) {
                hasInsertPrivilege = false;
                log.warn("User [" + userName +"] has no 'delete' privilege on table[" + tableName + "], errorMessage:[{}]", e.getMessage());
            }
        }
        try {
            connection.close();
        } catch (SQLException e) {
            log.warn("connection close failed, " + e.getMessage());
        }
        return hasInsertPrivilege;
    }

    /**
     * Retrieve a JDBC column value from a ResultSet, using the specified value type.
     * <p>Uses the specifically typed ResultSet accessor methods, falling back to
     * {@link #getResultSetValue(java.sql.ResultSet, int)} for unknown types.
     * <p>Note that the returned value may not be assignable to the specified
     * required type, in case of an unknown type. Calling code needs to deal
     * with this case appropriately, e.g. throwing a corresponding exception.
     * @param rs is the ResultSet holding the data
     * @param index is the column index
     * @param requiredType the required value type (may be {@code null})
     * @return the value object (possibly not of the specified required type,
     * with further conversion steps necessary)
     * @throws SQLException if thrown by the JDBC API
     * @see #getResultSetValue(ResultSet, int)
     */
    @Nullable
    public static Object getResultSetValue(ResultSet rs, int index, @Nullable Class<?> requiredType) throws SQLException {
        if (requiredType == null) {
            return getResultSetValue(rs, index);
        }

        Object value;

        // Explicitly extract typed value, as far as possible.
        if (String.class == requiredType) {
            return rs.getString(index);
        }
        else if (boolean.class == requiredType || Boolean.class == requiredType) {
            value = rs.getBoolean(index);
        }
        else if (byte.class == requiredType || Byte.class == requiredType) {
            value = rs.getByte(index);
        }
        else if (short.class == requiredType || Short.class == requiredType) {
            value = rs.getShort(index);
        }
        else if (int.class == requiredType || Integer.class == requiredType) {
            value = rs.getInt(index);
        }
        else if (long.class == requiredType || Long.class == requiredType) {
            value = rs.getLong(index);
        }
        else if (float.class == requiredType || Float.class == requiredType) {
            value = rs.getFloat(index);
        }
        else if (double.class == requiredType || Double.class == requiredType ||
                Number.class == requiredType) {
            value = rs.getDouble(index);
        }
        else if (BigDecimal.class == requiredType) {
            return rs.getBigDecimal(index);
        }
        else if (java.sql.Date.class == requiredType) {
            return rs.getDate(index);
        }
        else if (java.sql.Time.class == requiredType) {
            return rs.getTime(index);
        }
        else if (java.sql.Timestamp.class == requiredType || java.util.Date.class == requiredType) {
            return rs.getTimestamp(index);
        }
        else if (byte[].class == requiredType) {
            return rs.getBytes(index);
        }
        else if (Blob.class == requiredType) {
            return rs.getBlob(index);
        }
        else if (Clob.class == requiredType) {
            return rs.getClob(index);
        }
        else if (requiredType.isEnum()) {
            // Enums can either be represented through a String or an enum index value:
            // leave enum type conversion up to the caller (e.g. a ConversionService)
            // but make sure that we return nothing other than a String or an Integer.
            Object obj = rs.getObject(index);
            if (obj instanceof String) {
                return obj;
            }
            else if (obj instanceof Number) {
                // Defensively convert any Number to an Integer (as needed by our
                // ConversionService's IntegerToEnumConverterFactory) for use as index
                return NumberUtils.convertNumberToTargetClass((Number) obj, Integer.class);
            }
            else {
                // e.g. on Postgres: getObject returns a PGObject but we need a String
                return rs.getString(index);
            }
        }

        else {
            // Some unknown type desired -> rely on getObject.
            try {
                return rs.getObject(index, requiredType);
            }
            catch (AbstractMethodError err) {
                log.debug("JDBC driver does not implement JDBC 4.1 'getObject(int, Class)' method", err);
            }
            catch (SQLFeatureNotSupportedException ex) {
                log.debug("JDBC driver does not support JDBC 4.1 'getObject(int, Class)' method", ex);
            }
            catch (SQLException ex) {
                log.debug("JDBC driver has limited support for JDBC 4.1 'getObject(int, Class)' method", ex);
            }

            // Corresponding SQL types for JSR-310 / Joda-Time types, left up
            // to the caller to convert them (e.g. through a ConversionService).
            String typeName = requiredType.getSimpleName();
            if ("LocalDate".equals(typeName)) {
                return rs.getDate(index);
            }
            else if ("LocalTime".equals(typeName)) {
                return rs.getTime(index);
            }
            else if ("LocalDateTime".equals(typeName)) {
                return rs.getTimestamp(index);
            }

            // Fall back to getObject without type specification, again
            // left up to the caller to convert the value if necessary.
            return getResultSetValue(rs, index);
        }

        // Perform was-null check if necessary (for results that the JDBC driver returns as primitives).
        return (rs.wasNull() ? null : value);
    }

    /**
     * Retrieve a JDBC column value from a ResultSet, using the most appropriate
     * value type. The returned value should be a detached value object, not having
     * any ties to the active ResultSet: in particular, it should not be a Blob or
     * Clob object but rather a byte array or String representation, respectively.
     * <p>Uses the {@code getObject(index)} method, but includes additional "hacks"
     * to get around Oracle 10g returning a non-standard object for its TIMESTAMP
     * datatype and a {@code java.sql.Date} for DATE columns leaving out the
     * time portion: These columns will explicitly be extracted as standard
     * {@code java.sql.Timestamp} object.
     * @param rs is the ResultSet holding the data
     * @param index is the column index
     * @return the value object
     * @throws SQLException if thrown by the JDBC API
     * @see java.sql.Blob
     * @see java.sql.Clob
     * @see java.sql.Timestamp
     */
    @Nullable
    public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        String className = null;
        if (obj != null) {
            className = obj.getClass().getName();
        }
        if (obj instanceof Blob) {
            Blob blob = (Blob) obj;
            obj = blob.getBytes(1, (int) blob.length());
        }
        else if (obj instanceof Clob) {
            Clob clob = (Clob) obj;
            obj = clob.getSubString(1, (int) clob.length());
        }
        else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
            obj = rs.getTimestamp(index);
        }
        else if (className != null && className.startsWith("oracle.sql.DATE")) {
            String metaDataClassName = rs.getMetaData().getColumnClassName(index);
            if ("java.sql.Timestamp".equals(metaDataClassName) || "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
                obj = rs.getTimestamp(index);
            }
            else {
                obj = rs.getDate(index);
            }
        }
        else if (obj instanceof java.sql.Date) {
            if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
                obj = rs.getTimestamp(index);
            }
        }
        return obj;
    }

}
