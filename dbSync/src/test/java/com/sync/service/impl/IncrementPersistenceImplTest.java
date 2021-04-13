package com.sync.service.impl;

import com.sync.service.SyncTaskInfoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-03-15 14:07
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class IncrementPersistenceImplTest {

    @Autowired
    private SyncTaskInfoService syncTaskInfoService;

    @Test
    public void readIncrementVal() {
//        incrementPersistence.writeIncrementVal(1,new SyncIncrement(),"test");
    }
}