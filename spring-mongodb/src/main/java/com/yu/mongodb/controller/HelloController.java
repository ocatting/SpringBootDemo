package com.yu.mongodb.controller;

import com.yu.mongodb.Model.ResponseResult;
import com.yu.mongodb.entity.User;
import com.yu.mongodb.service.OperationService;
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
    private OperationService operationService;

    @RequestMapping("/add")
    public Object add (){
        User user = new User();
        user.setId(1);
        user.setName("你好");
        user.setSex("男");

        operationService.add(user);
        return ResponseResult.success(null);
    }

    @RequestMapping("/update")
    public Object update (){
        User user = new User();
        user.setId(1);
        user.setName("你好");
        user.setSex("男");

        operationService.add(user);
        return ResponseResult.success(null);
    }


    @RequestMapping("/h")
    public Object h (){
        operationService.findAll();
        return ResponseResult.success(null);
    }


}
