package com.yu.bloomfilter.controller;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@RestController
public class HelloController {

    @Autowired
    protected RedisTemplate<String,String> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;


}
