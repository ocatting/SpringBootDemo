package com.sync.core;

import com.sync.core.channel.BufferedRecordExchanger;
import com.sync.core.domain.Configuration;
import com.sync.core.utils.VMInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

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

    public TaskContainerRunner(Configuration configuration,BufferedRecordExchanger bufferedRecordExchanger,ICaller caller){
        this.configuration = configuration;
        this.bufferedRecordExchanger = bufferedRecordExchanger;
        this.caller = caller;
    }

    @Override
    public void run() {
        try {
            doRun();
            // 统计 与 检查执行状态
            while (this.caller.readAlive()) {
                // 周期统计
                log.info("DESC: {}",bufferedRecordExchanger.printDesc());
                Thread.sleep(2000);
            }
            bufferedRecordExchanger.shutdown();
        } catch (Exception e) {
            log.error("运行失败:{}",e.getMessage(),e);
        } finally {
            // 最后打印cpu的平均消耗，GC的统计
            VMInfo vmInfo = VMInfo.getVmInfo();
            if (vmInfo != null) {
                vmInfo.getDelta(false);
                log.info(vmInfo.totalString());
            }
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

}
