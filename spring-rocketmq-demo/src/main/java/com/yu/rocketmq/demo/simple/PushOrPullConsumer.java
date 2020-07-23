package com.yu.rocketmq.demo.simple;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 服务端推送模式 获取消费数据
 * @Author Yan XinYu
 **/
@Slf4j
public class PushOrPullConsumer {

    private String groupid = "groupid_producer_demo";
    private String namesrvAddr = "192.168.124.101:9876";
    private String topicName = "topic_sync_demo";

    @Test
    public void push_subscribe() throws MQClientException, InterruptedException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupid);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.subscribe(topicName,"*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt msg : list){
                    log.info("收到消息:{}",new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();

        log.info("consumer started");

        TimeUnit.MINUTES.sleep(30);
    }

    private static final Map<MessageQueue,Long> offsetTable = new HashMap<>();

    @Test
    public void pull_subscribe() throws MQClientException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer(groupid);
        consumer.setNamesrvAddr(namesrvAddr);

        consumer.start();

        Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues(topicName);
        for (MessageQueue msg : messageQueues){
            SINGLE_MQ:
            while (true) {
                try{
                    PullResult pullResult = consumer.pullBlockIfNotFound(msg, null, getMessageQueueOffset(msg), 32);
                    log.info("pullResult:{}",pullResult);
                    putMessageQueueOffset(msg,pullResult.getNextBeginOffset());

                    switch (pullResult.getPullStatus()){
                        case FOUND:
                            List<MessageExt> msgFoundList = pullResult.getMsgFoundList();
                            for(MessageExt m : msgFoundList){
                                log.info("pull 收到消息:{}",m);
                            }
                            break ;
                        case NO_MATCHED_MSG:
                            break ;
                        case NO_NEW_MSG:
                            break ;
                        case OFFSET_ILLEGAL:
                            break ;
                        default:
                            break ;
                    }
                } catch (Exception e){
                    log.error("pull 异常:{}",e);
                }
            }
        }
        consumer.shutdown();

    }

    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        offsetTable.put(mq, offset);
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = offsetTable.get(mq);
        if (offset != null)
            return offset;
        return 0;
    }


}
