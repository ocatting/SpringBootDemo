package com.project.mybatis.controller;

import com.project.mybatis.entity.Test;
import com.project.mybatis.service.option1.TestService;
import com.project.mybatis.service.option2.Test2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RestController
public class HelloController {

    @Autowired private TestService testService1;
    @Autowired private Test2Service testService2;

//    @Transactional(transactionManager = "jtaTransactionManager")
    @RequestMapping("hello")
    public String hello(){

        log.info("test1 =====>:{}",testService1.findById(1));
        Test test = new Test();
        test.setId(2);
        test.setName("1:test1");
        testService1.save(test);
        int i = 1/0;
        System.out.println(i);

        log.info("test2 =====>:{}",testService2.findById(1));
        test.setName("2:test2");
        testService2.save(test);

        return "hello";
    }
}
