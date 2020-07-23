package com.yu.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: 文档型数据库
 * 主要针对 它的增删改查
 * @Author Yan XinYu
 **/
@SpringBootApplication
public class MongoDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoDbApplication.class,args);
    }
}
