package com.yu.rocketmq.demo.schedule;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
public class SchedulePushConsumer {

    private String groupid = "groupid_producer_demo";
    private String namesrvAddr = "192.168.124.101:9876";
    private String topicName = "topic_sync_demo";

    /**
     * 消费方式上不会有什么改变
     * @throws MQClientException
     * @throws InterruptedException
     */
    @Test
    public void push_subscribe() throws MQClientException, InterruptedException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupid);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.subscribe(topicName,"*");

        consumer.registerMessageListener((List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext)-> {
            for (MessageExt msg : list){
                log.info("收到消息:{}",new String(msg.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();

        log.info("consumer started");

        TimeUnit.MINUTES.sleep(30);
    }
}
