package com.sync.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sync.common.CommonResult;
import com.sync.common.PageVo;
import com.sync.entity.SyncDb;
import com.sync.service.SyncDbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @Description: 数据库操作
 * @Author: Yan XinYu
 * @Date: 2021-03-18 12:25
 */
@Slf4j
@Controller
@RequestMapping("/db")
public class DbController {

    @Autowired
    private SyncDbService syncDbService;

    @GetMapping
    public String info(){
        return "db/db.index";
    }

    @PostMapping("/page")
    @ResponseBody
    public PageVo<SyncDb> page (Integer current, Integer size){
        Page<SyncDb> page = new Page<>(current == null ? 1:current,size == null ? 10:size);
        syncDbService.page(page);
        return PageVo.create(page.getRecords(),page.getTotal(),page.getTotal());
    }

    /**
     * 测试数据库连接
     * @param dbId 数据库配置ID
     * @return 成功/失败
     */
    @GetMapping("/test/{dbId}")
    @ResponseBody
    public CommonResult<String> test(@PathVariable("dbId") Integer dbId) throws SQLException {
        try {
            boolean b = syncDbService.connectTest(dbId);
            return b?CommonResult.success():CommonResult.failed("isValid is fail");
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @PostMapping("/add")
    @ResponseBody
    public CommonResult<String> add (SyncDb syncDb){
        syncDbService.save(syncDb);
        return CommonResult.success();
    }

    @PostMapping("/del/{dbId}")
    @ResponseBody
    public CommonResult<String> del (@PathVariable("dbId") Integer dbId){
        syncDbService.removeById(dbId);
        return CommonResult.success();
    }

    @PostMapping("/update")
    @ResponseBody
    public CommonResult<String> update (@RequestBody SyncDb syncDb){
        syncDbService.updateById(syncDb);
        return CommonResult.success();
    }

}
