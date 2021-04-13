package com.sync.core.db.rdb;

import com.sync.core.JdbcFactory;
import com.sync.core.db.Reader;
import com.sync.core.db.RecordSender;
import com.sync.core.element.*;
import com.sync.core.utils.DBUtil;
import com.sync.entity.SyncReadConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;

/**
 * @Description: Rdb读写
 * @Author: Yan XinYu
 * @Date: 2021-03-04 10:48
 */
@Slf4j
public class RdbReader implements Reader {

    private static final int FETCH_SIZE = 1000;

    private String dataBaseType;
    private String mandatoryEncoding;

    private Connection conn;

    private String querySql;

    @Override
    public void init(SyncReadConfig readConfig){

        this.conn = JdbcFactory.getConnection(readConfig.getSyncDb());

        this.dataBaseType = readConfig.getSyncDb().getDbType();

        this.mandatoryEncoding = readConfig.getMandatoryEncoding();

        this.querySql = readConfig.getQuerySql();
    }

    @Override
    public void startRead(RecordSender recordSender) {

        log.info("querySql: {}",querySql);

        log.info("Begin to read record by Sql: [{}] .",querySql);

        int columnNumber = 0;
        ResultSet rs = null;
        try {
            rs = DBUtil.query(conn, querySql, FETCH_SIZE);

            ResultSetMetaData metaData = rs.getMetaData();
            columnNumber = metaData.getColumnCount();

            while (rs.next()) {
                this.transportOneRecord(recordSender, rs, metaData, columnNumber, mandatoryEncoding);
            }

            log.info("Finished read record by Sql: [{}\n] .",querySql);
            rs.close();
        } catch (Exception e) {
            throw new RuntimeException(String.format("%s \n querySql:%s",this.dataBaseType,querySql),e);
        } finally {
            DBUtil.closeDBResources(null,conn);
        }
    }

    @Override
    public void shutdown() {
//        DBUtil.closeDBResources(null,conn);
    }

    protected void transportOneRecord(RecordSender recordSender, ResultSet rs,
                                      ResultSetMetaData metaData, int columnNumber, String mandatoryEncoding) {
        Record record = buildRecord(recordSender,rs,metaData,columnNumber,mandatoryEncoding);
        recordSender.sendToWriter(record);
    }

    protected final byte[] EMPTY_CHAR_ARRAY = new byte[0];

    protected Record buildRecord(RecordSender recordSender,ResultSet rs, ResultSetMetaData metaData, int columnNumber, String mandatoryEncoding) {

        Record record = recordSender.createRecord();
        try {
            for (int i = 1; i <= columnNumber; i++) {
                String columnName = metaData.getColumnName(i);

                switch (metaData.getColumnType(i)) {

                    case Types.CHAR:
                    case Types.NCHAR:
                    case Types.VARCHAR:
                    case Types.LONGVARCHAR:
                    case Types.NVARCHAR:
                    case Types.LONGNVARCHAR:
                        String rawData;
                        if(StringUtils.isBlank(mandatoryEncoding)){
                            rawData = rs.getString(i);
                        }else{

                            rawData = new String((rs.getBytes(i) == null ? EMPTY_CHAR_ARRAY :
                                    rs.getBytes(i)), mandatoryEncoding);
                        }
                        record.addColumn(new StringColumn(columnName,rawData));
                        break;

                    case Types.CLOB:
                    case Types.NCLOB:
                        record.addColumn(new StringColumn(columnName,rs.getString(i)));
                        break;

                    case Types.SMALLINT:
                    case Types.TINYINT:
                    case Types.INTEGER:
                    case Types.BIGINT:
                        record.addColumn(new LongColumn(columnName,rs.getString(i)));
                        break;

                    case Types.NUMERIC:
                    case Types.DECIMAL:
                        record.addColumn(new DoubleColumn(columnName,rs.getString(i)));
                        break;

                    case Types.FLOAT:
                    case Types.REAL:
                    case Types.DOUBLE:
                        record.addColumn(new DoubleColumn(columnName,rs.getString(i)));
                        break;

                    case Types.TIME:
                        record.addColumn(new DateColumn(columnName,rs.getTime(i)));
                        break;

                    // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
                    case Types.DATE:
                        if (metaData.getColumnTypeName(i).equalsIgnoreCase("year")) {
                            record.addColumn(new LongColumn(columnName,rs.getInt(i)));
                        } else {
                            record.addColumn(new DateColumn(columnName,rs.getDate(i)));
                        }
                        break;

                    case Types.TIMESTAMP:
                        record.addColumn(new DateColumn(columnName,rs.getTimestamp(i)));
                        break;

                    case Types.BINARY:
                    case Types.VARBINARY:
                    case Types.BLOB:
                    case Types.LONGVARBINARY:
                        record.addColumn(new BytesColumn(columnName,rs.getBytes(i)));
                        break;

                    // warn: bit(1) -> Types.BIT 可使用BoolColumn
                    // warn: bit(>1) -> Types.VARBINARY 可使用BytesColumn
                    case Types.BOOLEAN:
                    case Types.BIT:
                        record.addColumn(new BoolColumn(columnName,rs.getBoolean(i)));
                        break;

                    case Types.NULL:
                        String stringData = null;
                        if(rs.getObject(i) != null) {
                            stringData = rs.getObject(i).toString();
                        }
                        record.addColumn(new StringColumn(columnName,stringData));
                        break;

                    default:
                        throw new RuntimeException(String.format(
                                                "您的配置文件中的列配置信息有误. 因为不支持数据库读取这种字段类型. 字段名:[%s], 字段名称:[%s], 字段Java类型:[%s]. 请尝试使用数据库函数将其转换datax支持的类型 或者不同步该字段 .",
                                                metaData.getColumnName(i),
                                                metaData.getColumnType(i),
                                                metaData.getColumnClassName(i)));
                }
            }
        } catch (Exception e) {
                log.debug("read data " + record.toString()
                        + " occur exception:", e);
        }
        return record;
    }
}
