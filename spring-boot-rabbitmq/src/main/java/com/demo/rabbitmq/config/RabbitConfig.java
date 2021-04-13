package com.demo.rabbitmq.config;

import com.demo.rabbitmq.constants.RabbitMQConstants;
import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2020-12-30 17:40
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue fanoutQueue(){
        return new Queue(RabbitMQConstants.TEST_QUEUE,true,false,false);
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(RabbitMQConstants.EXCHANGE_QUEUE,true,false);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
    }


}
