package com.sync.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sync.common.PageVo;
import com.sync.entity.SyncTaskLog;
import com.sync.service.SyncTaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 日志信息
 * @Author: Yan XinYu
 * @Date: 2021-03-18 18:08
 */
@Slf4j
@Controller
@RequestMapping("/log")
public class LogController {

    @Autowired
    private SyncTaskLogService syncTaskLogService;

    @RequestMapping
    public String info(){
        return "tasklog/tasklog.index";
    }

    @PostMapping("/page")
    @ResponseBody
    public PageVo<SyncTaskLog> page(Integer current, Integer size){
        Page<SyncTaskLog> page = new Page<>(current == null ? 1:current,size == null ? 10:size);
        syncTaskLogService.page(page);
        return PageVo.create(page.getRecords(),page.getTotal(),page.getTotal());
    }
}
