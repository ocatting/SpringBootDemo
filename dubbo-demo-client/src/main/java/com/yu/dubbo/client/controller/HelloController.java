package com.yu.dubbo.client.controller;

import com.yu.dubbo.service.SpringbootDubboService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@RestController
public class HelloController {

    @Reference(version = "0.0.1")
    private SpringbootDubboService springbootDubboService;

    @RequestMapping("/h")
    public String hello(){
        return "hello："+springbootDubboService.getName();
    }

}
