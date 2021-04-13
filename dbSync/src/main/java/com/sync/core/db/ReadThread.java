package com.sync.core.db;

import com.sync.entity.SyncReadConfig;

/**
 * @Description: 读线程
 * @Author: Yan XinYu
 * @Date: 2021-03-20 15:10
 */

public class ReadThread extends Thread {

    private final Reader reader;
    private final SyncReadConfig config;
    private final RecordSender recordSender;

    public ReadThread(Reader reader, SyncReadConfig config , RecordSender recordSender, String threadName){
        this.reader = reader;
        this.config = config;
        this.recordSender = recordSender;
        setName(threadName);
    }

    @Override
    public void run() {
        this.reader.init(config);
        this.reader.startRead(recordSender);
        this.reader.shutdown();
    }
}
