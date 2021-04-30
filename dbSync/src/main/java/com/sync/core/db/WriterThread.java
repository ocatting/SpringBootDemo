package com.sync.core.db;

import com.sync.core.TaskCollector;
import com.sync.entity.SyncWriteConfig;

/**
 * @Description: 写线程
 * @Author: Yan XinYu
 * @Date: 2021-03-20 15:05
 */

public class WriterThread extends Thread {

    private final Writer writer;
    private final SyncWriteConfig config;
    private final RecordReceiver lineReceiver;
    private final TaskCollector taskCollector;

    public WriterThread(TaskCollector taskCollector,Writer writer, SyncWriteConfig config,RecordReceiver lineReceiver, String threadName){
        this.taskCollector = taskCollector;
        this.writer = writer;
        this.config = config;
        this.lineReceiver = lineReceiver;
        setName(threadName);
    }

    @Override
    public void run() {
        writer.init(taskCollector,config);
        // 执行前置
        writer.beforeStart(config);
        // 写数据，如果没有数据了就停止;
        writer.startWrite(lineReceiver);
        // 监听管道，如果管道停止了，那么结束写线程。
        while (!lineReceiver.isStop()){
            writer.startWrite(lineReceiver);
        }
        // 执行后置
        writer.postStart(config);
        writer.shutdown();
    }
}
