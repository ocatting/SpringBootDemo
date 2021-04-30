package com.sync.core;

import com.sync.core.channel.BufferedRecordExchanger;
import com.sync.core.domain.Configuration;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 单线程处理
 * @Author: Yan XinYu
 * @Date: 2021-03-03 20:46
 */
public class StandAloneScheduler implements ProcessInner {

    private final Configuration configuration;
    private final ICaller caller;
    private final BufferedRecordExchanger bufferedRecordExchanger;

    public StandAloneScheduler(Configuration configuration,ICaller caller) {
        this.configuration = configuration;
        this.caller = caller;
        this.bufferedRecordExchanger = new BufferedRecordExchanger(configuration.getTaskId(),configuration.getLimitTotalMemoryBytes());
    }

    @Override
    public void startTask() {
        bufferedRecordExchanger.setMapping(configuration.getMapping());
        new TaskContainerRunner(configuration,bufferedRecordExchanger,caller).run();
    }

    @Override
    public void shutdown() {
        this.bufferedRecordExchanger.shutdown();
    }


}
