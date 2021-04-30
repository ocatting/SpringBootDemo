package com.sync.core.db;

import com.sync.entity.SyncReadConfig;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-03-03 22:28
 */
public interface Reader {

    /**
     * 初始化读配置信息
     * @param readConfig 读配置
     */
    void init(SyncReadConfig readConfig);

    /**
     * 开始读取数据
     * @param recordSender 发送数据工具
     */
    void startRead(RecordSender recordSender);

    /**
     * 停止
     */
    void shutdown();

    /**
     * 获取rs.next(),最后使用时间
     * @return nano
     */
    long getRsNextLastTime();

}
