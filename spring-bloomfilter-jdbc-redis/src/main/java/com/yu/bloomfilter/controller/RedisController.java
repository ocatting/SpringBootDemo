package com.yu.bloomfilter.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedissonClient redissonClient;

    private String redis_order_path = "order_001";

    @RequestMapping("/lock")
    public void lock(){
        //分布式锁的实现
        RLock lock = redissonClient.getLock(redis_order_path);
        try {
            lock.lock();

            TimeUnit.SECONDS.sleep(12);


        } catch (InterruptedException e) {
            log.info("锁订单异常:",e);
        } finally {
            lock.unlock();
        }

    }

}
