package com.sync.core.db;

import com.sync.core.TaskCollector;
import com.sync.core.element.Record;
import com.sync.core.utils.ListTriple;
import com.sync.entity.SyncWriteConfig;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

/**
 * @Description: 写数据
 * @Author: Yan XinYu
 * @Date: 2021-03-03 22:42
 */
public interface Writer {

    /**
     * 参数初始化
     * @param writeConfig 写配置
     */
    void init(TaskCollector taskCollector, SyncWriteConfig writeConfig);

    /**
     * 执行前执行
     * @param writeConfig 写配置
     */
    void beforeStart(SyncWriteConfig writeConfig);

    /**
     * 执行后执行
     * @param writeConfig 写配置
     */
    void postStart(SyncWriteConfig writeConfig);

    /**
     * 从缓存中写数据
     * @param lineReceiver 缓存行数据
     */
    void startWrite(RecordReceiver lineReceiver);

    /**
     * 直接写入数据
     * @param resultSetMetaData 数据类型
     * @param writeBuffer 行数据
     */
    void startWrite(ListTriple resultSetMetaData, List<Record> writeBuffer);

    /**
     * 停止
     */
    void shutdown();
}
