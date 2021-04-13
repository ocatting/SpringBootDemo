package com.sync.core.db;

import com.sync.core.element.Record;
import com.sync.core.utils.RemoteUtil;
import com.sync.entity.SyncReadConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 调用远程方法去另一个服务读取数据
 * @Author: Yan XinYu
 * @Date: 2021-03-23 9:21
 */
@Slf4j
public class RemoteReadThread extends Thread {

    private final SyncReadConfig config;
    private final RecordSender recordSender;

    private volatile boolean running;

    public RemoteReadThread(SyncReadConfig config , RecordSender recordSender, String threadName){
        this.config = config;
        this.recordSender = recordSender;
        setName(threadName);
    }

    @Override
    public void run() {

        String uuid = UUID.randomUUID().toString();

        while (running){
            try {
                // 打开远程读线程读数据
                List<Record> records = RemoteUtil.readData(uuid, config);

                if(recordSender.isStop()){
                    sendData(records);
                    break;
                }
                if(records == null || records.isEmpty()){
                    TimeUnit.SECONDS.sleep(1);
                    continue;
                }
                sendData(records);
            } catch (InterruptedException e) {
                log.info("读线程被中断:{}",e.getMessage(),e);
            }
        }
    }

    private void sendData(List<Record> records){
        for (Record record : records) {
            if (record.isTerminate()){
                running = false;
                break;
            }
            // 发送数据到写线程。
            recordSender.sendToWriter(record);
        }
    }
}
