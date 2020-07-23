package com.undertow.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RestController
@RequestMapping("/hello")
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/testa")
    public String testa (){
        restTemplate.getForEntity("http://127.0.0.1/as",String.class);
        return "testa";
    }
}
