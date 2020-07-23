package com.yu.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@RestController
public class HelloController {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @RequestMapping("/h")
    public String h(){

        //向指定主题发送消息 ,指定分区 0, 指定key,
        kafkaTemplate.send("kafka_topic",0,"key","this is my data...");

        return "hello,kafka";
    }
}
