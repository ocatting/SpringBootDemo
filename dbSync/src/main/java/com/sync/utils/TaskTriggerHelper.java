package com.sync.utils;

import com.sync.entity.SyncTaskInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 任务触发
 * @Author: Yan XinYu
 * @Date: 2021-03-10 11:21
 */
@Slf4j
public class TaskTriggerHelper {

    private static final Map<Integer, TaskThread> JOB_THREAD_REPOSITORY = new ConcurrentHashMap<Integer, TaskThread>();

    /**
     * 仅仅允许单机串行
     * @param syncTaskInfo 任务配置信息
     */
    public static void trigger(SyncTaskInfo syncTaskInfo){

        TaskThread taskThread = JOB_THREAD_REPOSITORY.get(syncTaskInfo.getId());

        if(taskThread!=null && taskThread.isAlive()){
            log.info("Task: {}:{},线程任然在运行.",syncTaskInfo.getId(),syncTaskInfo.getTaskDesc());
            return ;
        }

        newJobThread(syncTaskInfo);
    }

    public static void newJobThread(SyncTaskInfo syncTaskInfo){
        TaskThread newJobThread = new TaskThread(syncTaskInfo);
        newJobThread.start();
        log.info("start task id:{}",syncTaskInfo.getId());

        TaskThread oldJobThread = JOB_THREAD_REPOSITORY.put(syncTaskInfo.getId(), newJobThread);
        if (oldJobThread != null) {
            oldJobThread.toStop(null);
            oldJobThread.interrupt();
        }
    }


}
