package com.yu.security.controller;

import com.yu.security.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@RestController
public class HelloController {

    @PostMapping("/hello")
    public String login(){
        return "hello nihao ";
    }

    @RequestMapping("json")
    public Result json(){
        return Result.fair();
    }

    @RequestMapping("/signin")
    public String signin(){
        return "signin";
    }

    @RequestMapping("/signout")
    public String signout(){
        return "signout";
    }
}
