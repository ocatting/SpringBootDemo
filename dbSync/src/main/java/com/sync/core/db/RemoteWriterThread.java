package com.sync.core.db;

import com.sync.core.element.Column;
import com.sync.core.element.Record;
import com.sync.core.exception.RemoteInvokeException;
import com.sync.core.exception.RemoteWriteException;
import com.sync.core.utils.DataBaseType;
import com.sync.core.utils.RemoteUtil;
import com.sync.entity.SyncDb;
import com.sync.entity.SyncWriteConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

/**
 * @Description: 调用远程方法让另外一个服务去 写数据
 * @Author: Yan XinYu
 * @Date: 2021-03-20 18:31
 */
@Slf4j
public class RemoteWriterThread extends Thread {

    private final SyncWriteConfig config;
    private final RecordReceiver lineReceiver;
    private final String dataBaseType;

    protected Triple<List<String>, List<Integer>, List<String>> resultSetMetaData;
    private Integer columnNumber;
    private final String table;
    private final List<String> columns;
    private final String execSql;
    private final SyncDb syncDb;

    private static final Integer BATCH_SIZE = 1000;

    public RemoteWriterThread(SyncWriteConfig config, RecordReceiver lineReceiver, String threadName){
        this.config = config;
        this.lineReceiver = lineReceiver;
        setName(threadName);

        this.dataBaseType = config.getSyncDb().getDbType();

        this.table = config.getTable();

        this.columns = config.getColumns();

        this.execSql = config.getExecSql();

        this.syncDb = config.getSyncDb();
    }

    @Override
    public void run() {

        String uuid = UUID.randomUUID().toString();

        log.info("before_sql exec:{}",config.getBeforeSql());
        // 前置执行
        RemoteUtil.execSql(uuid,config,config.getBeforeSql());

        // 抓取 字段 类型
        this.resultSetMetaData = RemoteUtil.getColumnProperty(syncDb.getRemoteAddress(),config);
        this.columnNumber = resultSetMetaData.getRight().size();

        while (!lineReceiver.isStop()){
            List<Record> writeBuffer = new ArrayList<>(BATCH_SIZE);
            try {
                Record record;
                while ((record = lineReceiver.getFromReader()) != null) {

                    writeBuffer.add(record);

                    if (writeBuffer.size() >= BATCH_SIZE) {
                        doWriteDate(uuid,config,resultSetMetaData,writeBuffer);
                        writeBuffer.clear();
                    }
                }
                if (!writeBuffer.isEmpty()) {
                    doWriteDate(uuid,config,resultSetMetaData,writeBuffer);
                    writeBuffer.clear();
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                writeBuffer.clear();
            }
        }

        // 后置执行
        RemoteUtil.execSql(uuid,config,config.getPostSql());
    }

    private void doWriteDate(String uuid,SyncWriteConfig config,Triple<List<String>, List<Integer>, List<String>> resultSetMetaData,List<Record> writeBuffer){
        try {
            RemoteUtil.writeDate(uuid,config,resultSetMetaData,writeBuffer);
        } catch (RemoteInvokeException e) {
            // 断开连接,就不要传输了，关闭管道。
            this.lineReceiver.shutdown();
        } catch (RemoteWriteException e) {
            // 可能数据有问题，单条发送试试
            for(Record record : writeBuffer){
                try {
                    RemoteUtil.writeDate(uuid,config,resultSetMetaData,Collections.singletonList(record));
                } catch (Exception i) {
                    log.debug(i.toString());
                    // 这里记录脏数据
                    try {
                        String fillSql = convertPlaceholders(execSql, record);
                        log.warn("dirty data : taskId:[{}], Record:{} ",lineReceiver.getTaskId(),fillSql);
                    } catch (SQLException ignored) {}
                }
            }
        } catch (Exception e){
            throw new RuntimeException("写数据异常，通常是写配置不正确导致",e);
        }
    }

    public String convertPlaceholders(String sql,Record record) throws SQLException {
        List<Object> columns = new ArrayList<>();
        for (int i = 0; i < this.columnNumber; i++) {
            String columnName = this.resultSetMetaData.getLeft().get(i);
            int columnSqlType = this.resultSetMetaData.getMiddle().get(i);
            columns.add(fillColumnType(i,columnSqlType,record.getColumn(columnName)));
        }
        // 占位符
        char placeholder = '?';
        // 统计占位符数量
        int count = countSql(sql,placeholder);
        if(count != columns.size()){
            throw new RuntimeException("字段不一致异常");
        }
        // 替换占位符
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i< sql.length();i++){
            char c = sql.charAt(i);
            if( c == placeholder ){
                sb.append(columns.get(i));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    protected Object fillColumnType(int columnIndex, int columnSqlType, Column column) throws SQLException {
        java.util.Date utilDate;
        // 查询字段可能与插入字段不一致，忽略掉
        if(column == null){
            return null;
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
            case Types.BOOLEAN:
                return column.asString();

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
                    return null;
                } else {
                    return strValue;
                }

                //tinyint is a little special in some database like mysql {boolean->tinyint(1)}
            case Types.TINYINT:
                Long longValue = column.asLong();
                if (null == longValue) {
                    return null;
                } else {
                    return longValue.toString();
                }

                // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
            case Types.DATE:
                if (this.resultSetMetaData.getRight().get(columnIndex)
                        .equalsIgnoreCase("year")) {
                    if (column.asBigInteger() == null) {
                        return null;
                    } else {
                        return column.asBigInteger().intValue();
                    }
                } else {
                    java.sql.Date sqlDate = null;
                    try {
                        utilDate = column.asDate();
                    } catch (RuntimeException e) {
                        throw new SQLException(String.format("Date 类型转换错误：[%s]", column));
                    }

                    if (null != utilDate) {
                        sqlDate = new java.sql.Date(utilDate.getTime());
                    }
                    return sqlDate;
                }

            case Types.TIME:
                java.sql.Time sqlTime = null;
                try {
                    utilDate = column.asDate();
                } catch (RuntimeException e) {
                    throw new SQLException(String.format("TIME 类型转换错误：[%s]", column));
                }

                if (null != utilDate) {
                    sqlTime = new java.sql.Time(utilDate.getTime());
                }
                return sqlTime;

            case Types.TIMESTAMP:
                java.sql.Timestamp sqlTimestamp = null;
                try {
                    utilDate = column.asDate();
                } catch (RuntimeException e) {
                    throw new SQLException(String.format("TIMESTAMP 类型转换错误：[%s]", column));
                }

                if (null != utilDate) {
                    sqlTimestamp = new java.sql.Timestamp(utilDate.getTime());
                }
                return sqlTimestamp;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.BLOB:
            case Types.LONGVARBINARY:
                return column.asBytes();

            // warn: bit(1) -> Types.BIT 可使用setBoolean
            // warn: bit(>1) -> Types.VARBINARY 可使用setBytes
            case Types.BIT:
                if (this.dataBaseType.equals(DataBaseType.MySql.getTypeName())) {
                    return column.asBoolean();
                } else {
                    return column.asString();
                }
            default:

        }
        return null;
    }

    public static int countSql(String sql,char placeholder){
        int result = 0;
        for (int i = 0;i<sql.length();i++){
            if(placeholder == sql.charAt(i)){
                result ++;
            }
        }
        return result;
    }

}
