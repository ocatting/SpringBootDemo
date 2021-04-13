package com.sync.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sync.common.CommonResult;
import com.sync.common.PageVo;
import com.sync.entity.SyncTaskInfo;
import com.sync.service.SyncTaskInfoService;
import com.sync.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 任务信息
 * @Author: Yan XinYu
 * @Date: 2021-03-16 19:57
 */
@Slf4j
@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SyncTaskInfoService syncTaskInfoService;

    @GetMapping("/info")
    public String info(){
        return "task/taskInfo.index";
    }

    @PostMapping("/page")
    @ResponseBody
    public PageVo<SyncTaskInfo> page (Integer current,Integer size){
        Page<SyncTaskInfo> page = new Page<>(current == null ? 1:current,size == null ? 10:size);
        syncTaskInfoService.page(page);
        return PageVo.create(page.getRecords(),page.getTotal(),page.getTotal());
    }

    /**
     * 触发一次任务
     * @param taskId 任务ID
     * @return 成功/失败
     */
    @GetMapping("/trigger/{taskId}")
    @ResponseBody
    public CommonResult<String> trigger(@PathVariable("taskId") Integer taskId) {
        taskService.trigger(taskId);
        return CommonResult.success();
    }

    /**
     * 开始一个任务
     * @param taskId 任务ID
     * @return 成功/失败
     */
    @GetMapping("/start/{taskId}")
    @ResponseBody
    public CommonResult<String> start(@PathVariable("taskId") Integer taskId) {
        syncTaskInfoService.lambdaUpdate().set(SyncTaskInfo::getTriggerStatus,1).eq(SyncTaskInfo::getId,taskId);
        return CommonResult.success();
    }

    /**
     * 停止一个任务
     * @param taskId 任务ID
     * @return 成功/失败
     */
    @GetMapping("/stop/{taskId}")
    @ResponseBody
    public CommonResult<String> stop(@PathVariable("taskId") Integer taskId) {
        syncTaskInfoService.lambdaUpdate().set(SyncTaskInfo::getTriggerStatus,0).eq(SyncTaskInfo::getId,taskId);
        return CommonResult.success();
    }

}
