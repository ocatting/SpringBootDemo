package com.yu.rocketmq.demo.batch;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class BatchPushConsumer {

    private String groupid = "batch_send_groupid";
    private String namesrvAddr = "192.168.124.101:9876";
    private String topic = "topic_sync_demo";

    @Test
    public void getData() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupid);

        consumer.setNamesrvAddr(namesrvAddr);

        consumer.subscribe(topic, "*");

        consumer.registerMessageListener((List<MessageExt> msgs,ConsumeConcurrentlyContext context)-> {
            for (MessageExt msg : msgs){
                System.out.println("queueId=" + msg.getQueueId() + "," + new String(msg.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();

        System.out.printf("Consumer Started.%n");
    }
}
