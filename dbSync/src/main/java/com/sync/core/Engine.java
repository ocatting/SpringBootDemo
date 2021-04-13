package com.sync.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sync.core.domain.Configuration;
import com.sync.core.utils.WriterUtil;
import com.sync.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Description: 执行入口
 * @Author: Yan XinYu
 * @Date: 2021-03-03 20:23
 */
@Slf4j
public class Engine {

    private final SyncTaskInfo syncTaskInfo;

    private final SyncDb readDb;
    private final SyncDb writeDb;

    private final SyncReadConfig readConfig;
    private final SyncWriteConfig writeConfig;

    private final ICaller caller;

    public Engine(SyncTaskInfo syncTaskInfo,
                  SyncReadConfig readConfig, SyncWriteConfig writeConfig, SyncDb readDb, SyncDb writeDb) {
        this.syncTaskInfo = syncTaskInfo;
        this.readConfig = readConfig;
        this.writeConfig = writeConfig;

        this.readDb = readDb;
        this.writeDb = writeDb;

        this.caller = ICaller.getInstance(syncTaskInfo.getModel());
    }

    public void start(){

        try {

            log.info(" starts to do read Columns ...");

            List<String> readColumns = caller.getReadColumns(readDb, readConfig.getTable(),readConfig.getQuerySql());

            List<String> writeColumns = caller.getWriteColumns(writeDb,writeConfig.getTable());

            // 解析字段映射
            Map<String, String> mapping = JSON.parseObject(writeConfig.getMappingJson(), new TypeReference<Map<String, String>>() {});
            log.info(" column mapping json :{}",mapping);

            String readSql = getReadSql(readColumns,readConfig.getTable());
            readConfig.setSyncDb(readDb);
            readConfig.setQuerySql(readSql);
            log.info(" read sql :{}",readSql);

            String writeSqlTemplate = WriterUtil.getWriteTemplate(writeColumns,WriterUtil.INSERT_MODE,writeConfig.getTable(),writeDb.getDbType());
            writeConfig.setSyncDb(writeDb);
            writeConfig.setColumns(writeColumns);
            writeConfig.setExecSql(writeSqlTemplate);
            log.info(" write sql :{}",writeSqlTemplate);

            Configuration configuration = new Configuration();
            configuration.setTaskId(syncTaskInfo.getId());
            configuration.setReadConfig(readConfig);
            configuration.setWriteConfig(writeConfig);
            configuration.setReadDb(readDb);
            configuration.setWriterDb(writeDb);
            configuration.setReadSql(readSql);
            configuration.setWriteSqlTemplate(writeSqlTemplate);
            configuration.setMapping(mapping);

            log.info(" starts to do execute ...");

            // 先前置条件执行 ，清除3个月前的记录
            // DELETE FROM `device_electricity_record` WHERE RECORD_TIME < ADDDATE(CURDATE(), INTERVAL -90 DAY);

            StandAloneScheduler scheduler = new StandAloneScheduler(configuration,caller);
            scheduler.startTask();

            log.info(" task completed successfully.");
        } catch (Throwable e) {
            log.error("Engine:运行异常.{}",e.getMessage(),e);
            throw new RuntimeException("Engine 运行异常",e);
        }

    }

    public String getReadSql(List<String> columns,String table){
        StringBuilder result = new StringBuilder();
        result.append("SELECT ").append(StringUtils.join(columns, ","));
        result.append(" FROM `").append(table).append("`");

        if(StringUtils.isEmpty(syncTaskInfo.getIncrementCol())){
            return result.toString();
        }
        result.append(" WHERE 1=1");
        if(!StringUtils.isEmpty(syncTaskInfo.getIncrementVal())){
            result.append(" AND `").append(syncTaskInfo.getIncrementCol()).append("` >= '").append(syncTaskInfo.getIncrementVal()).append("'");
        }
        if(!StringUtils.isEmpty(syncTaskInfo.getMaxVal())){
            result.append(" AND `").append(syncTaskInfo.getIncrementCol()).append("` < '").append(syncTaskInfo.getMaxVal()).append("'");
        }
        return result.toString();
    }

}
