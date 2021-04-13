package com.sync.core;

import com.sync.core.db.*;
import com.sync.core.utils.DBUtil;
import com.sync.entity.SyncDb;
import com.sync.entity.SyncReadConfig;
import com.sync.entity.SyncWriteConfig;

import java.sql.Connection;
import java.util.List;

/**
 * @Description: 调用者
 * @Author: Yan XinYu
 * @Date: 2021-03-20 11:14
 */
public class LocalCaller implements ICaller {

    private Thread readerThread;
    private Thread writerThread;

    /**
     * 获取读库字段
     * @param syncDb
     * @param table
     * @param customSql
     * @return
     */
    @Override
    public List<String> getReadColumns(SyncDb syncDb, String table, String customSql){
        Connection conn = JdbcFactory.getConnection(syncDb);
        return DBUtil.getTableColumnsByConn(conn,table, customSql);
    }

    /**
     * 获取写库字段
     * @param syncDb
     * @param table
     * @return
     */
    @Override
    public List<String> getWriteColumns(SyncDb syncDb, String table){
        Connection conn = JdbcFactory.getConnection(syncDb);
        return DBUtil.getTableColumnsByConn(conn,table);
    }

    @Override
    public void initWriteThread(SyncDb writeDb, RecordReceiver recordReceiver, SyncWriteConfig config) {
        Writer writer = DbLoadFactory.getWriter(writeDb.getDbType());
        this.writerThread = new WriterThread(writer,config, recordReceiver,"writer");
        this.writerThread.start();
    }

    @Override
    public void initReadThread(SyncDb readDb, RecordSender recordSender, SyncReadConfig config) {
        Reader reader = DbLoadFactory.getReader(readDb.getDbType());
        this.readerThread = new ReadThread(reader,config,recordSender,"read");
        this.readerThread.start();
    }

    @Override
    public boolean readAlive() {
        return readerThread.isAlive();
    }

    @Override
    public boolean writeAlive() {
        return writerThread.isAlive();
    }

}
