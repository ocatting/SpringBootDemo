package com.demo.rabbitmq.controller;

import com.demo.rabbitmq.constants.RabbitMQConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2020-12-30 17:43
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/send/{str}")
    public String send(@PathVariable("str") String str){
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGE_QUEUE,"",str);
        return "success";
    }
}
