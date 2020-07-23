package com.yu.rocketmq.demo.simple;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 消息的生产者
 * @Author Yan XinYu
 **/
@Slf4j
public class SyncOrAsyncProducer {

    private String groupid = "groupid_producer_demo";
    private String namesrvAddr = "192.168.124.101:9876";
    private String topic = "topic_sync_demo";

    /**
     * 同步模式 发送消息
     */
    @Test
    public void sync_producer() throws InterruptedException, RemotingException,
            MQClientException, MQBrokerException, UnsupportedEncodingException {

        DefaultMQProducer producer = new DefaultMQProducer(groupid);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setSendMsgTimeout(20*1000);
        producer.start();

        String topic = "topic_sync_demo";
        //搜索日志时 tags,keys 会用到
        String tags = "sync_test";
        //搜索日志时可以理解为orderID
        String keys = "tag";
        String msg = "hello rocketMQ sync test";
        Message message = new Message(topic,tags,keys,
                msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult send = producer.send(message);
        log.info("发送消息结果:{}",send);

        producer.shutdown();
    }

    /**
     * 异步模式 发送消息
     */
    @Test
    public void async_producer() throws UnsupportedEncodingException, RemotingException, MQClientException, InterruptedException {
        //异步发送失败 重试次数
        DefaultMQProducer producer = new DefaultMQProducer(groupid);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setSendMsgTimeout(20*1000);
        producer.setRetryTimesWhenSendAsyncFailed(2);
        producer.start();

        String topic = "topic_async_demo";
        //搜索日志时 tags,keys 会用到
        String tags = "async_test";
        String keys = "tag";
        String msg = "hello rocketMQ async test";

        int count = 1;
        final CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            Message message = new Message(topic,tags,keys,
                    msg.getBytes(RemotingHelper.DEFAULT_CHARSET));

            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    log.info("async 发送消息成功: MessageId: {}",sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    countDownLatch.countDown();
                    log.error("消息发送失败:{}",e);
                }
            });
        }

        countDownLatch.await(10, TimeUnit.SECONDS);

    }
}
