package com.demo.cache.redis.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Component
public class RedisTool {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void set(String key ,Object value){
        redisTemplate.opsForValue().set(key,value);
    }
    public void set(String key ,Object value,Duration duration){
        redisTemplate.opsForValue().set(key,value, duration);
    }

    public void get(String key){
        redisTemplate.opsForValue().get(key);
    }

}
