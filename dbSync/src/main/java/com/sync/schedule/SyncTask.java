package com.sync.schedule;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sync.core.JdbcFactory;
import com.sync.core.cron.CronExpression;
import com.sync.entity.SyncDb;
import com.sync.entity.SyncTaskInfo;
import com.sync.service.*;
import com.sync.utils.DateUtil;
import com.sync.utils.TaskTriggerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 同步任务
 * @Author: Yan XinYu
 * @Date: 2021-03-10 10:46
 */
@Slf4j
@Component
public class SyncTask implements InitializingBean, DisposableBean {

    private final SyncTaskInfoService syncTaskInfoService;
    private final SyncDbService syncDbService;

    private static final String SCHEDULE_THREAD_NAME = "Schedule-pool-%d";

    public static final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2,
                        new ThreadFactoryBuilder().setNameFormat(SCHEDULE_THREAD_NAME).build());

    @Autowired
    public SyncTask(SyncTaskInfoService syncTaskInfoService, SyncDbService syncDbService) {
        this.syncTaskInfoService = syncTaskInfoService;
        this.syncDbService = syncDbService;
    }

    @Override
    public void destroy() throws Exception {
        log.info("destroy ....");
        scheduledExecutorService.shutdown();
        JdbcFactory.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("start ....");
        // 注册 db
        List<SyncDb> dbList = syncDbService.lambdaQuery().list();
        JdbcFactory.setRegisterDbAll(dbList);

        // 开始扫描定时任务去执行
        scheduledExecutorService.scheduleWithFixedDelay(()->{

            long nowTime = System.currentTimeMillis();

            List<SyncTaskInfo> syncTaskInfos = syncTaskInfoService.scheduleTaskQuery(nowTime, 10);

            // 如何处理 时钟回拨呢?
            for (SyncTaskInfo task : syncTaskInfos) {
                try {
                    long triggerNextTime = task.getTriggerNextTime().getTime();
                    if (nowTime > (triggerNextTime + 5000)) {
                        log.warn("schedule misfire, taskId = " + task.getId());
                    } else {
                        // 执行
                        log.info("触发任务:taskId:{} date:{}",task.getId(), DateUtil.format(new Date(),DateUtil.FORMAT_ONE));
                        TaskTriggerHelper.trigger(task);
                    }
                    // fresh next
                    refreshNextValidTime(task, new Date());
                    syncTaskInfoService.updateById(task);
                } catch (Exception e) {
                    log.error("task schedule error:{}",e.getMessage(), e);
                }
            }
        },5L,1L, TimeUnit.SECONDS);

    }

    /**
     * 刷新下次执行时间
     * @param task 任务
     * @param fromTime 下次执行时间
     */
    private void refreshNextValidTime(SyncTaskInfo task, Date fromTime) throws ParseException {
        Date nextValidTime = new CronExpression(task.getTaskCron()).getNextValidTimeAfter(fromTime);
        if (nextValidTime != null) {
            task.setTriggerLastTime(task.getTriggerNextTime());
            task.setTriggerNextTime(nextValidTime);
        } else {
            task.setTriggerStatus(0);
            task.setTriggerLastTime(new Date());
            task.setTriggerNextTime(new Date());
        }
    }

}
