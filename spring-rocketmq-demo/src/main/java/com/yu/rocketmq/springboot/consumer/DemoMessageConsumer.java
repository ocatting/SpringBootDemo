package com.yu.rocketmq.springboot.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Service
@RocketMQMessageListener(topic = "${tl.rocketmq.topic}",consumerGroup = "${rocketmq.producer.group}")
public class DemoMessageConsumer implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

    @Override
    public void onMessage(String message) {
        log.info("我是消费者:{}",message);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        log.info("prepareStart...");
        //设置消费其实位置&消费时间点位
        //consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
        //consumer.setConsumeTimestamp(UtilAll.timeMillisToHumanString3(System.currentTimeMillis()));
    }
}
