package com.sync.core;

import com.sync.core.db.RecordReceiver;
import com.sync.core.db.RecordSender;
import com.sync.entity.SyncDb;
import com.sync.entity.SyncReadConfig;
import com.sync.entity.SyncWriteConfig;

import java.util.List;

/**
 * @Description: 调用
 * @Author: Yan XinYu
 * @Date: 2021-03-20 14:25
 */

public interface ICaller {

    /**
     * 获取读表字段
     * @param syncDb
     * @param table
     * @param customSql
     * @return
     */
    List<String> getReadColumns(SyncDb syncDb, String table, String customSql);

    /**
     * 获取写表字段
     * @param syncDb
     * @param table
     * @return
     */
    List<String> getWriteColumns(SyncDb syncDb, String table);

    /**
     * 初始化工作写数据线程
     * @param writeDb 数据库配置
     * @param recordReceiver 数据传输
     * @param config 读配置
     */
    void initWriteThread(SyncDb writeDb, RecordReceiver recordReceiver, SyncWriteConfig config);

    /**
     * 初始化读数据线程
     * @param readDb 数据库
     * @param recordSender 数据传输
     * @param config 读配置
     */
    void initReadThread(SyncDb readDb, RecordSender recordSender, SyncReadConfig config);

    /**
     * 读存活
     * @return false
     */
    boolean readAlive();

    /**
     * 写存活
     * @return
     */
    boolean writeAlive();

    /**
     * 本地执行
     */
    int LOCAL_MODE = 0;
    /**
     * 远程执行
     */
    int REMOTE_MODE = 1;

    /**
     * 实例化一个代理工作
     * @param model 模式
     * @return 代理实例
     */
    static ICaller getInstance(int model,TaskCollector taskCollector){
        if(model == LOCAL_MODE){
            return new LocalCaller(taskCollector);
        } else if (model == REMOTE_MODE) {
            return new RemoteCaller(taskCollector);
        } else {
            throw new RuntimeException("传输模式选择错误");
        }
    }
}
