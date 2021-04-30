package com.sync.core;

import com.sync.core.channel.BufferedRecordExchanger;
import com.sync.core.domain.Configuration;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 任务运行容器
 * @Author: Yan XinYu
 * @Date: 2021-03-03 21:03
 */
@Slf4j
public class TaskContainerRunner implements Runnable {

    private final Configuration configuration;

    private final ICaller caller;

    private final BufferedRecordExchanger bufferedRecordExchanger;

    private final TaskCollector taskCollector;

    public TaskContainerRunner(Configuration configuration,BufferedRecordExchanger bufferedRecordExchanger,ICaller caller){
        this.configuration = configuration;
        this.bufferedRecordExchanger = bufferedRecordExchanger;
        this.caller = caller;
        this.taskCollector = new TaskCollector(configuration.getTaskId());
    }

    @Override
    public void run() {
        try {
            doRun();
            // 统计 与 检查执行状态
            while (this.caller.readAlive()) {
                // 周期统计
                log.info("DESC:{}",bufferedRecordExchanger.printDesc());
                Thread.sleep(5000);
            }
            bufferedRecordExchanger.shutdown();
            log.info("Stop DESC:{}",bufferedRecordExchanger.printDesc());
        } catch (Exception e) {
            log.error("运行失败:{}",e.getMessage(),e);
        }
    }

    private void doRun(){
        log.info("初始连接信息:...");

        // 先初始化写线程
        this.caller.initWriteThread(configuration.getWriterDb(),bufferedRecordExchanger,configuration.getWriteConfig());

        // 再初始化读线程
        this.caller.initReadThread(configuration.getReadDb(),bufferedRecordExchanger,configuration.getReadConfig());

        if (!this.caller.writeAlive()){
            throw new RuntimeException("开启写线程失败");
        }

        if (!this.caller.readAlive()){
            throw new RuntimeException("开启读线程失败");
        }

        log.info("read thread write thread start...");
    }

    private TaskCollector getTaskCollector(){
        return this.taskCollector;
    }

}
