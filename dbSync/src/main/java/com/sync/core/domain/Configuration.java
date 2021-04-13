package com.sync.core.domain;

import com.sync.entity.SyncDb;
import com.sync.entity.SyncReadConfig;
import com.sync.entity.SyncWriteConfig;
import lombok.Data;

import java.util.Map;

/**
 * @Description: 配置文件
 * @Author: Yan XinYu
 * @Date: 2021-03-15 20:35
 */
@Data
public class Configuration {

    /**
     * 限制总量内存大小 1g
     */
    private int limitTotalMemoryBytes = 1024 * 1024 * 1024;

    private int taskId;

    private SyncDb readDb;
    private SyncDb writerDb;

    private String readSql;
    private String writeSqlTemplate;

    private SyncReadConfig readConfig;
    private SyncWriteConfig writeConfig;

    private Map<String,String> mapping;

}
