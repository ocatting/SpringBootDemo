package com.undertow.demo.controller;

import com.undertow.demo.entity.Test;
import com.undertow.demo.service.TestService;
import com.undertow.demo.service.impl.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private TestService testService;
    @Autowired
    private HelloService helloService;


    @RequestMapping("/h")
    public String h (){
        log.info("hello : -----------");
        testService.execute();
        log.info("hello current thread: {}",Thread.currentThread().getName());
        return "1";
    }

    @RequestMapping("/c")
    public String c(){
        log.info("test:c");
        List<Test> tests = testService.selectById(1);
        log.info("c:{}",tests);
        return "c is successful";
    }

    @RequestMapping("/v")
    public String v(){
        log.info("test:v");
        Test test = new Test();
        test.setId(2);
        test.setName("熊猫");
        testService.insert(test);
        log.info("v:{}",test);
        return "v is successful";
    }

}
