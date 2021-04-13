package com.sync.core;

/**
 * @Description: 内部处理线程
 * @Author: Yan XinYu
 * @Date: 2021-03-03 20:44
 */
public interface ProcessInner {

    /**
     * 执行数据读写任务
     */
    void startTask();

    /**
     * 结束任务
     */
    void shutdown();


}
