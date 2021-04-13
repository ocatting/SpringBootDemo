package com.sync.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sync.entity.SyncReadConfig;
import com.sync.entity.SyncTaskInfo;
import com.sync.entity.SyncWriteConfig;
import com.sync.mapper.SyncTaskInfoMapper;
import com.sync.service.SyncReadConfigService;
import com.sync.service.SyncTaskInfoService;
import com.sync.service.SyncWriteConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * @Description: ${description}
 * @Author: Yan XinYu
 * @Date: 2021-03-10 10:44
 */
@Service
public class SyncTaskInfoServiceImpl extends ServiceImpl<SyncTaskInfoMapper, SyncTaskInfo> implements SyncTaskInfoService {

    @Autowired
    private SyncReadConfigService syncReadConfigService;
    @Autowired
    private SyncWriteConfigService syncWriteConfigService;

    /**
     * 状态 0停止 1运行
     */
    private static final Integer STATUS_RUNNING = 1;
    private static final Integer STATUS_STOP = 0;

    @Override
    public List<SyncTaskInfo> scheduleTaskQuery(long maxNextTime, int pageSize) {
        List<SyncTaskInfo> list = lambdaQuery().lt(SyncTaskInfo::getTriggerNextTime, new Date(maxNextTime))
                .eq(SyncTaskInfo::getTriggerStatus, STATUS_RUNNING)
                .last("limit " + pageSize)
                .list();
        // 可能因为配置异常而停止运行
        for (SyncTaskInfo syncTaskInfo : list) {
            try {
                getOtherData(syncTaskInfo);
            } catch (Exception e) {
                syncTaskInfo.setTriggerStatus(STATUS_STOP);
                syncTaskInfo.setRemark(StringUtils.abbreviate(e.getMessage(), 100));
                updateById(syncTaskInfo);
            }
        }
        return list;
    }

    @Override
    public SyncTaskInfo queryTask(Integer taskId) {
        SyncTaskInfo taskInfo = getById(taskId);
        getOtherData(taskInfo);
        return taskInfo;
    }

    private void getOtherData(SyncTaskInfo taskInfo) {
        SyncReadConfig readConfig = syncReadConfigService.getById(taskInfo.getReadConfId());
        SyncWriteConfig writeConfig = syncWriteConfigService.getById(taskInfo.getWriteConfId());

        Assert.notNull(readConfig, "The autoincrement field is not set");
        Assert.notNull(readConfig, "ReadConfig is null");
        Assert.notNull(readConfig, "WriteConfig is null");

        taskInfo.setSyncReadConfig(readConfig);
        taskInfo.setSyncWriteConfig(writeConfig);
    }
}






