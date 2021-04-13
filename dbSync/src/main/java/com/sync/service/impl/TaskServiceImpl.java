package com.sync.service.impl;

import com.sync.common.exception.ServiceException;
import com.sync.entity.SyncTaskInfo;
import com.sync.service.SyncTaskInfoService;
import com.sync.service.TaskService;
import com.sync.utils.TaskTriggerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 任务
 * @Author: Yan XinYu
 * @Date: 2021-03-16 20:01
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private SyncTaskInfoService syncTaskInfoService;

    @Override
    public void trigger(Integer taskId) {
        SyncTaskInfo task = syncTaskInfoService.queryTask(taskId);
        if(task == null){
            throw new ServiceException("未找到任务信息");
        }
        TaskTriggerHelper.trigger(task);
    }
}
