package com.sync.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sync.entity.SyncDb;

/**
 * @Description: 数据库配置
 * @Author: Yan XinYu
 * @Date: 2021-03-10 16:39
 */
public interface SyncDbService extends IService<SyncDb> {

    /**
     * 测试连接
     * @param dbId 一个数据库连接
     * @throws Exception 连接异常
     * @return true/false
     */
    boolean connectTest(Integer dbId) throws Exception;

}


