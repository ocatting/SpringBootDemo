package com.yu.distace.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: 地理位置，搜索附近人实现方案。
 * 使用 Redis GeoHash 高阶函数解决 地理位置方案（其实是用zset实现）
 * Redis HyperLogLog 解决统计问题
 * Redis Stream 结构，消费链表。(队列问题)
 * Redis 跳跃表，
 * Redis rehash 字典
 * Redis 发布订阅 
 * @Author Yan XinYu
 **/
@SpringBootApplication
public class RedisDistanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisDistanceApplication.class,args);
    }
}
