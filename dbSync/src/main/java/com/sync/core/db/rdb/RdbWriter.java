package com.sync.core.db.rdb;

import com.sync.core.JdbcFactory;
import com.sync.core.db.RecordReceiver;
import com.sync.core.db.Writer;
import com.sync.core.element.Column;
import com.sync.core.element.Record;
import com.sync.core.utils.DBUtil;
import com.sync.core.utils.DataBaseType;
import com.sync.entity.SyncDb;
import com.sync.entity.SyncWriteConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: rdb写入
 * @Author: Yan XinYu
 * @Date: 2021-03-04 14:25
 */
@Slf4j
public class RdbWriter implements Writer {

    private static final Integer BATCH_SIZE = 1000;

    private String dataBaseType;

    protected Triple<List<String>, List<Integer>, List<String>> resultSetMetaData;

    private String table;

    private Integer columnNumber;

    private List<String> columns;

    private String execSql;

    private SyncDb syncDb;

    @Override
    public void init( SyncWriteConfig writeConfig){

        this.dataBaseType = writeConfig.getSyncDb().getDbType();

        this.table = writeConfig.getTable();

        this.columns = writeConfig.getColumns();

        this.execSql = writeConfig.getExecSql();

        this.syncDb = writeConfig.getSyncDb();
    }

    @Override
    public void beforeStart(SyncWriteConfig writeConfig) {

        if(StringUtils.isEmpty(writeConfig.getBeforeSql())){
            return ;
        }
        log.info("before_sql exec:{}",writeConfig.getBeforeSql());
        try {
            Connection connection = JdbcFactory.getConnection(writeConfig.getSyncDb());
            DBUtil.executeSql(connection,writeConfig.getBeforeSql());
        } catch (SQLException e) {
            log.error("before_sql exception:{} ",writeConfig.getBeforeSql(),e);
        }
    }

    @Override
    public void postStart(SyncWriteConfig writeConfig) {
        if(StringUtils.isEmpty(writeConfig.getPostSql())){
            return ;
        }
        log.info("post_sql exec:{}",writeConfig.getPostSql());
        try {
            Connection connection = JdbcFactory.getConnection(writeConfig.getSyncDb());
            DBUtil.executeSql(connection,writeConfig.getPostSql());
        } catch (SQLException e) {
            log.error("post_sql exception:{} ",writeConfig.getPostSql(),e);
        }
    }

    @Override
    public void startWrite(RecordReceiver recordReceiver) {
        Connection connection = JdbcFactory.getConnection(syncDb);
        // 用于写入数据的时候的类型根据目的表字段类型转换
        this.resultSetMetaData = DBUtil.getColumnMetaData(connection,table, StringUtils.join(this.columns,","));

        this.columnNumber = resultSetMetaData.getRight().size();

        List<Record> writeBuffer = new ArrayList<>(BATCH_SIZE);
        try {
            Record record;
            while ((record = recordReceiver.getFromReader()) != null) {

                writeBuffer.add(record);

                if (writeBuffer.size() >= BATCH_SIZE) {
                    doBatchInsert(connection, writeBuffer);
                    writeBuffer.clear();
                }
            }
            if (!writeBuffer.isEmpty()) {
                doBatchInsert(connection, writeBuffer);
                writeBuffer.clear();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            writeBuffer.clear();
            DBUtil.closeDBResources(null,connection);
        }
    }

    @Override
    public void startWrite(Triple<List<String>, List<Integer>, List<String>> resultSetMetaData,List<Record> writeBuffer) {

        this.resultSetMetaData = resultSetMetaData;
        this.columnNumber = resultSetMetaData.getRight().size();

        if (writeBuffer != null && !writeBuffer.isEmpty()) {
            Connection connection = null;
            try {
                connection = JdbcFactory.getConnection(syncDb);
                doBatchInsert(connection, writeBuffer);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                writeBuffer.clear();
                DBUtil.closeDBResources(null,connection);
            }
        }
    }

    @Override
    public void shutdown() {
//        DBUtil.closeDBResources(null,connection);
    }

    protected void doBatchInsert(Connection connection, List<Record> buffer)
            throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(this.execSql);

            for (Record record : buffer) {
                preparedStatement = fillPreparedStatement(preparedStatement, record);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            log.error("回滚此次写入, 采用每次写入一行方式提交. 因为:{}", e.getMessage(),e);
            connection.rollback();
            doOneInsert(connection, buffer);
        } catch (Exception e) {
            log.error("写入数据失败:",e);
            throw new RuntimeException("写入数据失败", e);
        } finally {
            DBUtil.closeDBResources(preparedStatement, connection);
        }
    }

    protected void doOneInsert(Connection connection, List<Record> buffer) {
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(true);
            preparedStatement = connection.prepareStatement(this.execSql);

            for (Record record : buffer) {
                try {
                    preparedStatement = fillPreparedStatement(preparedStatement, record);
                    preparedStatement.execute();
                } catch (SQLException e) {
                    log.debug(e.toString());

//                    this.taskPluginCollector.collectDirtyRecord(record, e);
                } finally {
                    preparedStatement.clearParameters();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException( e);
        } finally {
            DBUtil.closeDBResources(preparedStatement, connection);
        }
    }

    protected PreparedStatement fillPreparedStatement(PreparedStatement preparedStatement, Record record) throws SQLException {
        for (int i = 0; i < this.columnNumber; i++) {
            String columnName = this.resultSetMetaData.getLeft().get(i);
            int columnSqlType = this.resultSetMetaData.getMiddle().get(i);
            preparedStatement = fillPreparedStatementColumnType(preparedStatement, i, columnSqlType, record.getColumn(columnName));
        }
        return preparedStatement;
    }

    protected PreparedStatement fillPreparedStatementColumnType(PreparedStatement preparedStatement, int columnIndex, int columnSqlType, Column column) throws SQLException {
        java.util.Date utilDate;
        // 查询字段可能与插入字段不一致，忽略掉
        if(column == null){
            return preparedStatement;
        }
        switch (columnSqlType) {
            case Types.CHAR:
            case Types.NCHAR:
            case Types.CLOB:
            case Types.NCLOB:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                preparedStatement.setString(columnIndex + 1, column
                        .asString());
                break;

            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                String strValue = column.asString();
                if ("".equals(strValue)) {
                    preparedStatement.setString(columnIndex + 1, null);
                } else {
                    preparedStatement.setString(columnIndex + 1, strValue);
                }
                break;

            //tinyint is a little special in some database like mysql {boolean->tinyint(1)}
            case Types.TINYINT:
                Long longValue = column.asLong();
                if (null == longValue) {
                    preparedStatement.setString(columnIndex + 1, null);
                } else {
                    preparedStatement.setString(columnIndex + 1, longValue.toString());
                }
                break;

            // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
            case Types.DATE:
                if (this.resultSetMetaData.getRight().get(columnIndex)
                        .equalsIgnoreCase("year")) {
                    if (column.asBigInteger() == null) {
                        preparedStatement.setString(columnIndex + 1, null);
                    } else {
                        preparedStatement.setInt(columnIndex + 1, column.asBigInteger().intValue());
                    }
                } else {
                    java.sql.Date sqlDate = null;
                    try {
                        utilDate = column.asDate();
                    } catch (RuntimeException e) {
                        throw new SQLException(String.format(
                                "Date 类型转换错误：[%s]", column));
                    }

                    if (null != utilDate) {
                        sqlDate = new java.sql.Date(utilDate.getTime());
                    }
                    preparedStatement.setDate(columnIndex + 1, sqlDate);
                }
                break;

            case Types.TIME:
                java.sql.Time sqlTime = null;
                try {
                    utilDate = column.asDate();
                } catch (RuntimeException e) {
                    throw new SQLException(String.format(
                            "TIME 类型转换错误：[%s]", column));
                }

                if (null != utilDate) {
                    sqlTime = new java.sql.Time(utilDate.getTime());
                }
                preparedStatement.setTime(columnIndex + 1, sqlTime);
                break;

            case Types.TIMESTAMP:
                java.sql.Timestamp sqlTimestamp = null;
                try {
                    utilDate = column.asDate();
                } catch (RuntimeException e) {
                    throw new SQLException(String.format(
                            "TIMESTAMP 类型转换错误：[%s]", column));
                }

                if (null != utilDate) {
                    sqlTimestamp = new java.sql.Timestamp(
                            utilDate.getTime());
                }
                preparedStatement.setTimestamp(columnIndex + 1, sqlTimestamp);
                break;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.BLOB:
            case Types.LONGVARBINARY:
                preparedStatement.setBytes(columnIndex + 1, column
                        .asBytes());
                break;

            case Types.BOOLEAN:
                preparedStatement.setString(columnIndex + 1, column.asString());
                break;

            // warn: bit(1) -> Types.BIT 可使用setBoolean
            // warn: bit(>1) -> Types.VARBINARY 可使用setBytes
            case Types.BIT:
                if (this.dataBaseType.equals(DataBaseType.MySql.getTypeName())) {
                    preparedStatement.setBoolean(columnIndex + 1, column.asBoolean());
                } else {
                    preparedStatement.setString(columnIndex + 1, column.asString());
                }
                break;
            default:
                throw new RuntimeException(String.format(
                                        "您的配置文件中的列配置信息有误. 因为不支持数据库写入这种字段类型. 字段名:[%s], 字段类型:[%d], 字段Java类型:[%s]. 请修改表中该字段的类型或者不同步该字段.",
                                        this.resultSetMetaData.getLeft()
                                                .get(columnIndex),
                                        this.resultSetMetaData.getMiddle()
                                                .get(columnIndex),
                                        this.resultSetMetaData.getRight()
                                                .get(columnIndex)));
        }
        return preparedStatement;
    }

}
