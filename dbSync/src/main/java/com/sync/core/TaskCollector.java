package com.sync.core;

import com.sync.core.element.Record;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 数据收集，比如脏数据
 * @Author: Yan XinYu
 * @Date: 2021-03-06 12:05
 */
@Slf4j
public class TaskCollector {

    /**
     * 任务编号
     */
    private final int taskId;

    public TaskCollector(int taskId) {
        this.taskId = taskId;
    }

    public void collectDirtyRecord(final Record dirtyRecord,
                                   final String errorMessage) {
        this.collectDirtyRecord(dirtyRecord, null, errorMessage);
    }

    public void collectDirtyRecord(final Record dirtyRecord,final Throwable t, final String errorMessage){
        log.warn("dirty data : taskId:[{}], Record:{} errMsg:{}",taskId,dirtyRecord,errorMessage);
    }

    public void collectDirtyRecord(final Record dirtyRecord, final Throwable t) {
        this.collectDirtyRecord(dirtyRecord, t, t.getMessage());
    }
}
