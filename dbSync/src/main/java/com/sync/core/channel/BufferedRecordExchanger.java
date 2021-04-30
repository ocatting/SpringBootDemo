package com.sync.core.channel;

import com.sync.core.db.RecordReceiver;
import com.sync.core.db.RecordSender;
import com.sync.core.element.Record;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 缓冲队列交换器
 * @Author: Yan XinYu
 * @Date: 2021-03-03 23:00
 */
public class BufferedRecordExchanger implements RecordSender, RecordReceiver {

    private static Class<? extends Record> RECORD_CLASS;

    private volatile boolean shutdown = false;

    private final int totalMemoryBytes;
    private static final AtomicInteger CURRENT_MEMORY_BYTES = new AtomicInteger(0);
    private Map<String,String> mapping;
    private final int taskId;
    private long totalNum = 0;

    /**
     * 传输数据管道
     */
    private final Channel channel;

    public BufferedRecordExchanger(int taskId,int totalMemoryBytes){
        this.taskId = taskId;
        this.totalMemoryBytes = totalMemoryBytes;
        this.channel = new MemoryChannel(taskId,null,null);
        try {
            RECORD_CLASS = (Class<? extends Record>) Class.forName("com.sync.core.element.Record");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("配置异常导致实例化行数据失败",e);
        }
    }

    @Override
    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    @Override
    public Record getFromReader() {
        Record record = channel.takeTimeout();
        if (record == null) {
            return null;
        }
        // 字段映射
        record.mapping(this.mapping);
        CURRENT_MEMORY_BYTES.addAndGet(- record.getMemorySize());
        return record;
    }

    @Override
    public boolean isStop() {
        return this.shutdown;
    }

    @Override
    public int getTaskId() {
        return this.taskId;
    }

    @Override
    public Record createRecord() {
        try {
            return BufferedRecordExchanger.RECORD_CLASS.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("创建行数据失败,通常是配置异常或服务器资源不够。");
        }
    }

    @Override
    public void sendToWriter(Record record) {
        if(shutdown){
            throw new RuntimeException("已停止写入数据");
        }
        Validate.notNull(record, "record不能为空.");
        try {
            while (true){
                if(CURRENT_MEMORY_BYTES.get() > totalMemoryBytes){
                    TimeUnit.MILLISECONDS.sleep(500);
                } else {
                    break;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("线程被中断",e);
        }
        CURRENT_MEMORY_BYTES.addAndGet(record.getMemorySize());
        this.channel.push(record);
        totalNum ++;
    }

    @Override
    public void flush() {
        if(shutdown){
            throw new RuntimeException("已停止写入数据");
        }
        this.channel.clear();
        CURRENT_MEMORY_BYTES.set(0);
    }

    @Override
    public void terminate() {
        if(shutdown){
            throw new RuntimeException("已停止写入数据");
        }
        Record record = createRecord();
        record.terminate(true);
        this.channel.pushTerminate(record);
    }

    @Override
    public boolean shutdown() {
        this.shutdown = true;
        return this.shutdown;
    }

    public String printDesc(){
        return " task:["+taskId+"] sent count:"+totalNum+", total:"+CURRENT_MEMORY_BYTES.get()+"/"+totalMemoryBytes + "," +channel.getMemoryBytesDesc() + " , " + "queue size:" + channel.getQueueSize();
    }
}
