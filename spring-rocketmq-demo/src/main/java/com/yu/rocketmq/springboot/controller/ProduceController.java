package com.yu.rocketmq.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RestController
public class ProduceController {

    @Value("${tl.rocketmq.topic}")
    private String springTopic;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("producer")
    public String producer(){
        log.info("生产者:");
        SendResult sendResult = rocketMQTemplate.syncSend(springTopic, "生产发送消息");
//        rocketMQTemplate.asyncSend(springTopic,call);
        return "send status:" + sendResult.getSendStatus();
    }
}
