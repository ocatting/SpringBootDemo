package com.demo.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: Redis各种方案的实现。
 *
 * 使用 Redis GeoHash 高阶函数解决 地理位置方案
 *      二维平面，通过画格子的方式切片，转换为一个一维结构。
 *
 * Redis HyperLogLog 解决统计问题
 *      原理：抛硬币连续为正面格式猜想结果；
 *      {
 *          页面1=["用户1","用户2","用户3",...],
 *          页面2=["用户4","用户1","用户5"],
 *          ...
 *      }
 *      根据页面的用户数，通过概率学 统计用户的访问量(连续为0的数);
 *
 * Redis Stream 结构，消费链表。(队列问题)
 *
 * Redis 跳跃表，从元数据中随机抽取一些数做索引;
 * zset 是一个有序非线性的数据结构,它底层核心的数据结构是跳表. 跳表（skiplist）是一个特殊的链表，
 * 相比一般的链表，有更高的查找效率，其效率可比拟于二叉查找树。
 *
 * Redis rehash 字典 hashmap key=value
 *
 * Redis 发布订阅 基于数据结构链表存放所有订阅的客户端，若数据修改则推送消息。
 *
 * @Author Yan XinYu
 **/
@SpringBootApplication
public class SpringRedisCacheApplication {

    /**
     * Reids 五中数据结构
     * String（动态字符串（SDS））,Hash(键值对),List(双向链表),
     * set(（数据结构，hash+链表）非重复集合),zset(有序非重复集合) 此集合可以：取交集，并集，差集
     * 高阶 stream(队列) geohash
     * Redis key 为hash结构
     * Redis Cluster 将所有数据划分为 16384 个 slots(槽位)，每个节点负责其中一部分槽位。
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringRedisCacheApplication.class,args);
    }
}
