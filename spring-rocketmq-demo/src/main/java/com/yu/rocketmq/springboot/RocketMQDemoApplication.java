package com.yu.rocketmq.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@SpringBootApplication
public class RocketMQDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketMQDemoApplication.class,args);
    }

    /**
     * RocketMQ 原理：
     * 刷盘机制，nio 零拷贝技术。
     * 读数据，分为两个文件，queue文件(文件队列)，commitlog(文件数据)文件 queue存放offset指向commitlog文件位置，读取数据长度。
     * index 索引存放 500w 的数据文件，2000w 的数据 采用 keyHash 数组与 数据链表，如果hash冲突,形成链表在client端接收时过滤。
     */
}
