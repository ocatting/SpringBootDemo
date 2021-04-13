package com.sync.core.channel;

/**
 * @Description: http 方式传输数据
 * @Author: Yan XinYu
 * @Date: 2021-03-19 16:44
 */
public class HttpChannel extends Channel {

    public HttpChannel(int taskId, Integer capacity, Integer limitBytesSize) {
        super(taskId, capacity, limitBytesSize);
    }


}
