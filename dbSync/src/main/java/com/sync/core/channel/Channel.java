package com.sync.core.channel;

import com.sync.core.element.Record;
import org.apache.commons.lang3.Validate;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 数据传输管道
 * @Author: Yan XinYu
 * @Date: 2021-03-19 16:40
 */
public abstract class Channel {

    /**
     * 任务ID
     */
    private int taskId;

    /**
     * 管道行数据容量
     */
    private int capacity = 10000;

    /**
     * 限制内存大小 256m
     */
    private int limitBytesSize = 256 * 1024 * 1024;

    /**
     * 管道占用内存容量
     */
    private final AtomicInteger memoryBytes = new AtomicInteger(0);

    /**
     * 队列
     */
    private final LinkedBlockingQueue<Record> queue;

    Channel (int taskId,Integer capacity,Integer limitBytesSize){
        this.taskId = taskId;
        if(capacity != null && capacity > this.capacity){
            this.capacity = capacity;
        }
        if(limitBytesSize != null){
            this.limitBytesSize = limitBytesSize;
        }
        this.queue = new LinkedBlockingQueue<>(this.capacity);
    }

    public int getQueueSize(){
        return this.queue.size();
    }

    /**
     * 占用内存
     * @return 描述
     */
    public String getMemoryBytesDesc(){
        return String.format("channel memoryBytes: %db/%db",memoryBytes.get(),limitBytesSize);
    }

    /**
     * 推送数据
     * @param record 行记录
     */
    public void push(final Record record) {
        try {
            // 判断是否超过指定容量，超过就等待。
            while (true){
                if(memoryBytes.get() > limitBytesSize){
                    TimeUnit.SECONDS.sleep(1);
                } else {
                    break;
                }
            }
            // 判断是否可以压入队列中，容量超过则等待压入。
            while (true){
                if (this.queue.offer(record,200,TimeUnit.MILLISECONDS)){
                    break;
                }
            }
            // 记录当前内存容量
            this.memoryBytes.addAndGet(record.getMemorySize());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    public void pushTerminate(final Record r) {
        Validate.notNull(r, "record不能为空.");
        this.push(r);
    }

    /**
     * 拉取数据
     * @return 返回记录
     */
    public Record takeTimeout() {
        try {
            Record record = this.queue.poll(1000,TimeUnit.MILLISECONDS);
            if (record == null) {
                return null;
            }
            if (record.isTerminate()){
                return null;
            }
            this.memoryBytes.addAndGet(- record.getMemorySize());
            return record;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    public void clear(){
        this.queue.clear();
        this.memoryBytes.set(0);
    }

}
