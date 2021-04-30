package com.sync.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sync.common.Constant;
import com.sync.core.JdbcFactory;
import com.sync.core.utils.DBUtil;
import com.sync.core.utils.ListTriple;
import com.sync.core.utils.ReadHelper;
import com.sync.entity.*;
import com.sync.mapper.SyncReadConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-03-15 23:49
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class SyncTaskInfoServiceTest {

    @Autowired
    private SyncTaskInfoService syncTaskInfoService;

    @Autowired
    private SyncGroupService syncGroupService;

    @Autowired
    private SyncReadConfigService syncReadConfigService;

    @Autowired
    private SyncReadConfigMapper syncReadConfigMapper;

    @Autowired
    private SyncWriteConfigService syncWriteConfigService;

    @Autowired
    private SyncDbService syncDbService;

    @Test
    public void jsonTest(){
        SyncDb syncDb = syncDbService.getById(5);

        SyncWriteConfig writeConfig = syncWriteConfigService.getById(17);

        Connection connection = JdbcFactory.getConnection(syncDb);

        List<String> writeColumns = DBUtil.getTableColumnsByConn(connection,writeConfig.getTable());

        writeConfig.setSyncDb(syncDb);
        writeConfig.setColumns(writeColumns);

        ListTriple resultSetMetaData = ReadHelper.columnProperty(writeConfig);

        String jsonString = JSON.toJSONString(resultSetMetaData);
        System.out.println("jsonString:"+jsonString);
        ListTriple triple = JSON.parseObject(jsonString, new TypeReference<ListTriple>() {});
        System.out.println("triple:"+triple);
    }

    @Test
    public void createTask(){

        Map<String,String> readWriteTable = new HashMap<>();
        readWriteTable.put("daily_check_detail","daily_check_detail");
        readWriteTable.put("daily_check_info","daily_check_info");
        readWriteTable.put("daily_check_line_info","daily_check_line_info");
        readWriteTable.put("daily_check_line_point_rel","daily_check_line_point_rel");
        readWriteTable.put("daily_check_line_user_rel","daily_check_line_user_rel");
        readWriteTable.put("daily_check_point_info","daily_check_point_info");
        readWriteTable.put("daily_check_term_info","daily_check_term_info");
        readWriteTable.put("daily_check_term_point_rel","daily_check_term_point_rel");
        readWriteTable.put("daily_check_user_rel","daily_check_user_rel");

        readWriteTable.put("device_electricity_record","device_electricity_record");
        readWriteTable.put("device_hydrant_record","device_hydrant_record");
        readWriteTable.put("device_hydraulic_record","device_hydraulic_record");
        readWriteTable.put("device_info","device_info");
        readWriteTable.put("device_manhole_cover_record","device_manhole_cover_record");
        readWriteTable.put("device_together_set","device_together_set");
        readWriteTable.put("device_together_set_detail","device_together_set_detail");
        readWriteTable.put("device_type_info","device_type_info");
        readWriteTable.put("device_video","device_video");
        readWriteTable.put("device_warning_deal_log","device_warning_deal_log");
        readWriteTable.put("device_warning_log","device_warning_log");
        readWriteTable.put("device_warning_record","device_warning_record");

        String generalIncrementCol = "create_time";

        // 新建一个分组
        SyncGroup syncGroup = new SyncGroup();
        syncGroup.setId(1);
        syncGroup.setGroupName("ITSS数据同步到G20数据库");
        syncGroup.setCreateTime(new Date());
        syncGroupService.save(syncGroup);

        for (Map.Entry<String, String> entry : readWriteTable.entrySet()) {
            String readTable = entry.getKey();
            String writeTable = entry.getValue();

//            SyncTaskInfo syncTaskInfo = syncTaskInfoService.getById(2);

            // 新建一个写配置
            SyncWriteConfig writeConfig = new SyncWriteConfig();
            writeConfig.setCreateTime(LocalDateTime.now());
            writeConfig.setTable(writeTable);
            writeConfig.setWriteMode("insert");
            writeConfig.setExecSql(null);
            writeConfig.setMappingJson(null);
            syncWriteConfigService.save(writeConfig);

            // 新建一个读配置
            SyncReadConfig readConfig = new SyncReadConfig();
            readConfig.setCreateTime(LocalDateTime.now());
            readConfig.setTable(readTable);
            readConfig.setQuerySql(null);
            readConfig.setLimitMemoryBytes(256);
            readConfig.setMandatoryEncoding("utf-8");

            syncReadConfigService.save(readConfig);

            // 新建一个任务
            SyncTaskInfo syncTaskInfo = new SyncTaskInfo();
            syncTaskInfo.setId(2);
            syncTaskInfo.setTaskDesc("模板");
            syncTaskInfo.setTaskCron("0/5 * * * * ? *");
            syncTaskInfo.setReadDbId(3);
            syncTaskInfo.setReadConfId(readConfig.getId());
            syncTaskInfo.setWriteDbId(5);
            syncTaskInfo.setWriteConfId(writeConfig.getId());
            syncTaskInfo.setModel(0);
            syncTaskInfo.setTriggerStatus(0);
            syncTaskInfo.setIncrementCol(generalIncrementCol);
            syncTaskInfo.setIncrementType(Constant.SYNC_INCREMENT_TYPE_TIME);

            syncTaskInfoService.save(syncTaskInfo);

        }

    }

    @Test
    public void insertTest(){
        // 新建一个读配置
        SyncReadConfig readConfig = new SyncReadConfig();
        readConfig.setCreateTime(LocalDateTime.now());
        readConfig.setTable("tt");
        readConfig.setQuerySql(null);
        readConfig.setLimitMemoryBytes(256);
        readConfig.setMandatoryEncoding("utf-8");

        syncReadConfigMapper.insert(readConfig);

        System.out.println("getId:"+readConfig.getId());
    }

    @Test
    public void scheduleTaskQuery() {
        long nowTime = System.currentTimeMillis();
        List<SyncTaskInfo> syncTaskInfos = syncTaskInfoService.scheduleTaskQuery(nowTime + 5000, 10);
        for (SyncTaskInfo syncTaskInfo : syncTaskInfos) {
            System.out.println(JSON.toJSONString(syncTaskInfo));
//            System.out.println(DateUtil.format(new Date(syncTaskInfo.getTriggerNextTime().getTime()),DateUtil.FORMAT_ONE));
        }
    }
}