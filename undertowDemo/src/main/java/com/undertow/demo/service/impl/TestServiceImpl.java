package com.undertow.demo.service.impl;

import com.undertow.demo.entity.Test;
import com.undertow.demo.mapper.TestMapper;
import com.undertow.demo.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.applet.Main;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private HelloService helloService;

    @Async
    @Override
    public void execute() {
        log.info("Test execute ====> start");
        log.info("Test execute current thread: {}",Thread.currentThread().getName());
//        IntStream.range(0, 5).forEach(d -> {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
        helloService.execute();
        log.info("Test execute ====> end");
    }

    @Autowired
    TestMapper testMapper;

    @Override
    public List<Test> selectById(Integer id) {
        return testMapper.selectById(id);
    }


    @Override
    public void insert(Test test) {
        testMapper.insert(test);
        System.out.println(1/0);
    }

}
