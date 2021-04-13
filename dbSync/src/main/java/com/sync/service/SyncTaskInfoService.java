package com.sync.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sync.entity.SyncTaskInfo;

import java.util.List;

/**
 * @Description: 任务配置
 * @Author: Yan XinYu
 * @Date: 2021-03-10 10:44
 */
public interface SyncTaskInfoService extends IService<SyncTaskInfo> {

    /**
     * 查询要执行的任务
     *
     * @param maxNextTime 最近开始的任务数据
     * @param limit       分页大小
     * @return
     */
    List<SyncTaskInfo> scheduleTaskQuery(long maxNextTime, int limit);

    /**
     * 查询任务
     *
     * @param taskId 任务ID
     * @return
     */
    SyncTaskInfo queryTask(Integer taskId);

}






