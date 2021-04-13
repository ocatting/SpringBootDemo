package com.sync.service;

/**
 * @Description: 任务
 * @Author: Yan XinYu
 * @Date: 2021-03-16 20:00
 */

public interface TaskService {

    /**
     * 触发一次任务
     * @param taskId
     */
    void trigger(Integer taskId);

}
