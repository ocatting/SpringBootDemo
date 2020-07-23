package com.yu.distace.mongodb.controller;

import com.yu.distace.mongodb.service.MongoDbDistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@RestController
public class HelloController {

    @Autowired
    private MongoDbDistanceService mongoDbDistanceService;


    @RequestMapping("/h")
    public String h (){
        mongoDbDistanceService.test();
        return "success";
    }

    @RequestMapping("/find")
    public String find(){
        mongoDbDistanceService.nearSphere();
        return "success";
    }
}
