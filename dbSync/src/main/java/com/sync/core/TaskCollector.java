package com.sync.core;

import com.sync.core.element.Record;

/**
 * @Description: 数据收集，比如脏数据
 * @Author: Yan XinYu
 * @Date: 2021-03-06 12:05
 */

public class TaskCollector {

    public void collectDirtyRecord(final Record dirtyRecord,
                                   final String errorMessage) {
        this.collectDirtyRecord(dirtyRecord, null, errorMessage);
    }

    public void collectDirtyRecord(final Record dirtyRecord,final Throwable t, final String errorMessage){

    }

    public void collectDirtyRecord(final Record dirtyRecord, final Throwable t) {
        this.collectDirtyRecord(dirtyRecord, t, t.getMessage());
    }
}
