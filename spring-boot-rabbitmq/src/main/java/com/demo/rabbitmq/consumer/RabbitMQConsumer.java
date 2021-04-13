package com.demo.rabbitmq.consumer;

import com.demo.rabbitmq.constants.RabbitMQConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2020-12-30 17:33
 */
@Slf4j
@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = RabbitMQConstants.TEST_QUEUE)
    public void testHandler(String str){
        log.info("testHandler:{}",str);
    }
}
