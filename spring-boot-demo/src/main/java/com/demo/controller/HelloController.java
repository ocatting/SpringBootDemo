package com.demo.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@RestController
//@RequestMapping("/hello/{name}")
public class HelloController {

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable("name") String name){
        return "Hello get " + name ;
    }
}
