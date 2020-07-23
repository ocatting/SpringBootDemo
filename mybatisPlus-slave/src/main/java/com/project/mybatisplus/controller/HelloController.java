package com.project.mybatisplus.controller;

import com.project.mybatisplus.dto.ResponseMsg;
import com.project.mybatisplus.entity.TestUser;
import com.project.mybatisplus.entity.User;
import com.project.mybatisplus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private UserService userService;

    @RequestMapping(value = "/h/{id}",produces = "application/json")
    public Object h(@PathVariable("id") int id){

//        List<User> users = userService.selectById(id);
//        log.info("controller master : {}",users);
//
//        List<User> users1 = userService.selectByOrder("1");
//        log.info("controller slave : {}",users1);

//        List<String> list = new ArrayList<>(1);
//
//        list.add(null);
        TestUser testUser = new TestUser();
        testUser.setTime(LocalDateTime.now());
        return new ResponseMsg(testUser);
//        return list;
    }


}
