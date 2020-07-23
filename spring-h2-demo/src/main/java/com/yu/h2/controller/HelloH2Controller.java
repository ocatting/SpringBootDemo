package com.yu.h2.controller;

import com.yu.h2.entity.User;
import com.yu.h2.repository.UserRepository;
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
public class HelloH2Controller {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/hello")
    public String hello(){
        List<User> all = userRepository.findAll();
        for (User user:all) {
            log.info("信息:{}",user.toString());
        }
        return "你好";
    }
}
