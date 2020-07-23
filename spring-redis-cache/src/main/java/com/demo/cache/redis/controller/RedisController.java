package com.demo.cache.redis.controller;

import com.demo.cache.redis.model.ResponseMsg;
import com.demo.cache.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    /**
     * redis 操作
     * @return
     */
    @RequestMapping("r")
    public ResponseMsg r(){

        redisService.add();

        return ResponseMsg.success(null);
    }
}
