package com.sync.core.channel;

/**
 * @Description: 通过内存的方式数据传输管道
 * @Author: Yan XinYu
 * @Date: 2021-03-06 14:21
 */
public class MemoryChannel extends Channel {

    public MemoryChannel(int taskId,Integer capacity,Integer limitBytesSize){
        super(taskId, capacity, limitBytesSize);
    }

}
