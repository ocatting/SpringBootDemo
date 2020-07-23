package com.jetty.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/h")
    public String h (){
        log.info("hello : -----------");
        return "hello:1";
    }
}
