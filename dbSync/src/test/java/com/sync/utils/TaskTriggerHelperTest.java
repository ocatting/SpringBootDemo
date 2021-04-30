package com.sync.utils;

import com.sync.core.JdbcFactory;
import com.sync.core.utils.DBUtil;
import com.sync.entity.SyncDb;
import com.sync.entity.SyncTaskInfo;
import com.sync.service.SyncDbService;
import com.sync.service.SyncTaskInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.util.Date;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-03-31 15:19
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class TaskTriggerHelperTest {

    @Autowired
    private SyncTaskInfoService syncTaskInfoService;

    @Autowired
    private SyncDbService syncDbService;

    @Test
    public void trigger() {
        // 这个不会生效的，junit 不支持多线程
//        SyncTaskInfo task = syncTaskInfoService.queryTask(17);
        // 执行一次任务
//        TaskTriggerHelper.trigger(task);
    }

    @Test
    public void col(){


    }

    @Test
    public void createTaskTest(){

        String readConf = "";
        String writeConf = "";

        SyncTaskInfo syncTaskInfo = new SyncTaskInfo();
        syncTaskInfo.setId(2);
        syncTaskInfo.setTaskCron(null);
        syncTaskInfo.setTaskDesc("测试同步机制");
        
        // 选择数据库
        syncTaskInfo.setReadDbId(3);
        syncTaskInfo.setWriteDbId(2);
        syncTaskInfo.setModel(0);

        // 状态为启动
        syncTaskInfo.setTriggerStatus(1);

        syncTaskInfoService.save(syncTaskInfo);

    }

    @Test
    public void connectTest() {

        // 本地连接测试
        try {
            boolean b = syncDbService.connectTest(1);
            System.out.println("数据库连接结果:"+b);
        } catch (Exception e) {
            log.error("连接异常:{}",e.getMessage(),e);
        }

        // 远程连接测试

    }

    @Test
    public void createTest() {

        SyncDb syncDb = new SyncDb();
        syncDb.setName("bmTest");
        syncDb.setDbType("mysql");
        syncDb.setDriver("com.mysql.cj.jdbc.Driver");
        syncDb.setJdbcUrl("jdbc:mysql://119.3.1.135:3306/bm202012?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai");
        syncDb.setUsername("root");
        syncDb.setPassword("root");
        syncDb.setOpenRemote(false);
        syncDb.setRemoteAddress(null);
        syncDb.setCreateTime(new Date());
        syncDb.setRemark(null);

        syncDbService.save(syncDb);

//        SyncDb syncDbMapping = new SyncDb();
//        syncDb.setName("bmTest");
//        syncDb.setDbType("mysql");
//        syncDb.setDriver("com.mysql.cj.jdbc.Driver");
//        syncDb.setJdbcUrl("jdbc:mysql://119.3.1.135:3306/bm202012?useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai");
//        syncDb.setUsername("root");
//        syncDb.setPassword("root");
//        syncDb.setOpenRemote(false);
//        syncDb.setRemoteAddress(null);
//        syncDb.setCreateTime(new Date());
//        syncDb.setRemark(null);
//
//        syncDbService.save(syncDbMapping);
    }
}