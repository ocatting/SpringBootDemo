package com.sync.core;

import com.sync.core.db.*;
import com.sync.core.utils.DBUtil;
import com.sync.core.utils.RemoteUtil;
import com.sync.entity.SyncDb;
import com.sync.entity.SyncReadConfig;
import com.sync.entity.SyncWriteConfig;

import java.sql.Connection;
import java.util.List;

/**
 * @Description: 远程调用
 * @Author: Yan XinYu
 * @Date: 2021-03-20 16:10
 */

public class RemoteCaller implements ICaller {

    private Thread readerThread;
    private Thread writeThread;

    @Override
    public List<String> getReadColumns(SyncDb syncDb, String table, String customSql) {
        if (syncDb.getOpenRemote()) {
            return RemoteUtil.getColumns(syncDb.getRemoteAddress(),syncDb,table);
        }
        Connection conn = JdbcFactory.getConnection(syncDb);
        return DBUtil.getTableColumnsByConn(conn,table, customSql);
    }

    @Override
    public List<String> getWriteColumns(SyncDb syncDb, String table) {
        if (syncDb.getOpenRemote()) {
            return RemoteUtil.getColumns(syncDb.getRemoteAddress(),syncDb,table);
        }
        Connection conn = JdbcFactory.getConnection(syncDb);
        return DBUtil.getTableColumnsByConn(conn,table);
    }

    @Override
    public void initWriteThread(SyncDb writeDb, RecordReceiver recordReceiver, SyncWriteConfig config) {
        // 打开一个线程接收写数据，并发送到远程服务接口中。
        Writer write = DbLoadFactory.getWriter(writeDb.getDbType());
        if (writeDb.getOpenRemote()) {
            this.writeThread = new RemoteWriterThread(config,recordReceiver,"write");
        } else {
            this.writeThread = new WriterThread(write,config,recordReceiver,"write");
        }
        this.writeThread.start();
    }

    @Override
    public void initReadThread(SyncDb readDb, RecordSender recordSender, SyncReadConfig config) {
        // 打开一个线程一直读取 recordSender 中的数据，并发送到远程服务接口中。
        Reader reader = DbLoadFactory.getReader(readDb.getDbType());
        if (readDb.getOpenRemote()) {
            this.readerThread = new RemoteReadThread(config,recordSender,"read");
        } else {
            this.readerThread = new ReadThread(reader,config,recordSender,"read");
        }
        this.readerThread.start();
    }

    @Override
    public boolean readAlive() {
        return readerThread.isAlive();
    }

    @Override
    public boolean writeAlive() {
        return writeThread.isAlive();
    }

}
