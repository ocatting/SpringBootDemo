package com.project.slave.controller;

import com.project.slave.entity.User;
import com.project.slave.service.master.UserMainService;
import com.project.slave.service.slave.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    private UserService userService;

    @Autowired
    private UserMainService userMainService;

    @RequestMapping("/h/{id}")
    public Object h(@PathVariable("id") int id){

        List<User> users = userService.selectById(id);
        log.info("controller : {}",users);

        User user = new User();
        user.setName("你好");
        userMainService.save(user);

        return id;
    }




}
