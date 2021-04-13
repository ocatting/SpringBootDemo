package com.sync.utils;

import com.sync.common.Constant;
import com.sync.core.Engine;
import com.sync.core.JdbcFactory;
import com.sync.core.utils.ReaderUtil;
import com.sync.core.utils.RemoteUtil;
import com.sync.entity.*;
import com.sync.service.SyncTaskInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-03-10 12:04
 */
@Slf4j
public class TaskThread extends Thread {

    private volatile boolean toStop = false;
    private String stopReason;

    private final SyncTaskInfo syncTaskInfo;

    private SyncDb readDb;
    private SyncDb writeDb;

    private SyncReadConfig readConfig;
    private SyncWriteConfig writeConfig;

    private static final String INCREMENT_MAX_QUERY_SQL = "SELECT MAX(`#{col}`) as COL FROM `#{table}` ";

    public TaskThread(SyncTaskInfo syncTaskInfo){
        this.syncTaskInfo = syncTaskInfo;
    }

    public void preHandler(){

        this.readConfig = syncTaskInfo.getSyncReadConfig();
        this.writeConfig = syncTaskInfo.getSyncWriteConfig();

        this.readDb = JdbcFactory.get(syncTaskInfo.getReadDbId());
        this.writeDb = JdbcFactory.get(syncTaskInfo.getWriteDbId());
    }

    public int getTaskId(){
        return this.syncTaskInfo.getId();
    }


    public void toStop(String stopReason) {
        /**
         * Thread.interrupt只支持终止线程的阻塞状态(wait、join、sleep)，
         * 在阻塞出抛出InterruptedException异常,但是并不会终止运行的线程本身；
         * 所以需要注意，此处彻底销毁本线程，需要通过共享变量方式；
         */
        this.toStop = true;
        this.stopReason = stopReason;
    }

    @Override
    public void run() {
        // 获取上次执行时间或者自增ID；
        long currentTime = System.currentTimeMillis();
        try {
            preHandler();

            getMaxVal(currentTime);

            Engine engine = new Engine(syncTaskInfo,readConfig,writeConfig,readDb,writeDb);
            engine.start();

            // 从Spring IOC 中获取
            SyncTaskInfoService syncTaskInfoService = SpringBeanLocator.getBean(SyncTaskInfoService.class);

            if(!StringUtils.isEmpty(syncTaskInfo.getIncrementCol()) && syncTaskInfoService != null) {
                syncTaskInfo.setIncrementVal(syncTaskInfo.getMaxVal());
                syncTaskInfoService.updateById(syncTaskInfo);
            }
        } catch (Exception e) {
            log.error("读写配置异常:{}",e.getMessage(),e);
        } finally {
            long cost = System.currentTimeMillis() - currentTime;
            log.info("Task Complete! id:{},{},cost:{}:ms",syncTaskInfo.getId(),syncTaskInfo.getTaskDesc(),cost);
        }
    }

    private void getMaxVal(long currentTime){

        if(StringUtils.isEmpty(syncTaskInfo.getIncrementCol())){
            return;
        }

        if (syncTaskInfo.getIncrementType() == Constant.SYNC_INCREMENT_TYPE_TIME) {
            syncTaskInfo.setMaxVal(DateUtil.toString(currentTime));
        }

        if (syncTaskInfo.getIncrementType() == Constant.SYNC_INCREMENT_TYPE_AUTO_ID) {
            String maxId = getMaxIncrementVal(readDb,readConfig.getTable());
            syncTaskInfo.setMaxVal(maxId);
        }
    }

    private String getMaxIncrementVal(SyncDb syncDb, String table) {

        // 生成sql语句
        Properties properties = new Properties();
        properties.put("col",syncTaskInfo.getIncrementCol());
        properties.put("table",table);

        String querySql = StringHelper.replacePlaceholders(INCREMENT_MAX_QUERY_SQL,properties);
        log.info("increment querySql:{}",querySql);

        if (syncDb.getOpenRemote()) {
            return RemoteUtil.getIncrementVal(syncDb.getRemoteAddress(), syncDb, querySql);
        } else {
            return ReaderUtil.readIncrementValByOne(syncDb, querySql);
        }
    }

}
